package com.rbih_assessmenmt.loanengine.service;

import com.rbih_assessmenmt.loanengine.domain.entity.Applicant;
import com.rbih_assessmenmt.loanengine.domain.entity.LoanApplication;
import com.rbih_assessmenmt.loanengine.domain.entity.LoanRequest;
import com.rbih_assessmenmt.loanengine.domain.enums.ApplicationStatus;
import com.rbih_assessmenmt.loanengine.domain.enums.Riskband;
import com.rbih_assessmenmt.loanengine.dto.request.LoanApplicationRequest;
import com.rbih_assessmenmt.loanengine.dto.response.LoanApplicationResponse;
import com.rbih_assessmenmt.loanengine.dto.response.OfferResponse;
import com.rbih_assessmenmt.loanengine.repository.LoanApplicationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class LoanApplicationService {

    private final EmiCalculatorService emiCalculatorService;
    private final RiskbandService RiskbandService;
    private final InterestRateService interestRateService;
    private final EligibilityService eligibilityService;
    private final LoanApplicationRepository repository;

    public LoanApplicationService(EmiCalculatorService emiCalculatorService,
                                  RiskbandService RiskbandService,
                                  InterestRateService interestRateService,
                                  EligibilityService eligibilityService,
                                  LoanApplicationRepository repository) {
        this.emiCalculatorService = emiCalculatorService;
        this.RiskbandService = RiskbandService;
        this.interestRateService = interestRateService;
        this.eligibilityService = eligibilityService;
        this.repository = repository;
    }

    public LoanApplicationResponse process(LoanApplicationRequest request) {
        int creditScore = request.getApplicant().getCreditScore();
        Riskband Riskband = RiskbandService.classify(creditScore);

        BigDecimal loanAmount = BigDecimal.valueOf(request.getLoan().getAmount());
        BigDecimal interestRate = interestRateService.calculate(
                Riskband,
                request.getApplicant().getEmploymentType(),
                loanAmount
        );

        int tenureMonths = request.getLoan().getTenureMonths();
        BigDecimal emi = emiCalculatorService.calculate(loanAmount, interestRate, tenureMonths);

        List<String> rejectionReasons = eligibilityService.evaluate(request, emi);

        LoanApplication application = buildEntity(request);

        if (!rejectionReasons.isEmpty()) {
            application.setStatus(ApplicationStatus.REJECTED);
            application.setRiskband(null);
            repository.save(application);
            return LoanApplicationResponse.rejected(application.getId(), rejectionReasons);
        }

        BigDecimal totalPayable = emi.multiply(BigDecimal.valueOf(tenureMonths))
                .setScale(2, RoundingMode.HALF_UP);
        OfferResponse offer = new OfferResponse(interestRate, tenureMonths, emi, totalPayable);

        application.setStatus(ApplicationStatus.APPROVED);
        application.setRiskband(Riskband);
        repository.save(application);

        return LoanApplicationResponse.approved(application.getId(), Riskband, offer);
    }

    private LoanApplication buildEntity(LoanApplicationRequest request) {
        Applicant applicant = new Applicant();
        applicant.setName(request.getApplicant().getName());
        applicant.setAge(request.getApplicant().getAge());
        applicant.setMonthlyIncome(request.getApplicant().getMonthlyIncome());
        applicant.setEmploymentType(request.getApplicant().getEmploymentType());
        applicant.setCreditScore(request.getApplicant().getCreditScore());

        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setAmount(request.getLoan().getAmount());
        loanRequest.setTenureMonths(request.getLoan().getTenureMonths());
        loanRequest.setPurpose(request.getLoan().getPurpose());

        LoanApplication application = new LoanApplication();
        application.setApplicant(applicant);
        application.setLoanRequest(loanRequest);
        return application;
    }
}