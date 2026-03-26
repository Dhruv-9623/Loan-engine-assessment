package com.rbih_assessmenmt.loanengine.service;

import com.rbih_assessmenmt.loanengine.domain.enums.EmploymentType;
import com.rbih_assessmenmt.loanengine.domain.enums.LoanPurpose;
import com.rbih_assessmenmt.loanengine.dto.request.ApplicantRequest;
import com.rbih_assessmenmt.loanengine.dto.request.LoanApplicationRequest;
import com.rbih_assessmenmt.loanengine.dto.request.LoanRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EligibilityServiceTest {

    private EligibilityService eligibilityService;

    @BeforeEach
    void setUp() {
        eligibilityService = new EligibilityService();
    }

    private LoanApplicationRequest buildRequest(int age, int creditScore, double income, int tenureMonths) {
        ApplicantRequest applicant = new ApplicantRequest();
        applicant.setName("Test User");
        applicant.setAge(age);
        applicant.setCreditScore(creditScore);
        applicant.setMonthlyIncome(income);
        applicant.setEmploymentType(EmploymentType.SALARIED);

        LoanRequestDto loan = new LoanRequestDto();
        loan.setAmount(500000);
        loan.setTenureMonths(tenureMonths);
        loan.setPurpose(LoanPurpose.PERSONAL);

        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setApplicant(applicant);
        request.setLoan(loan);
        return request;
    }

    @Test
    void shouldRejectWhenCreditScoreBelowThreshold() {
        LoanApplicationRequest request = buildRequest(30, 550, 75000, 36);
        BigDecimal emi = new BigDecimal("16000");

        List<String> reasons = eligibilityService.evaluate(request, emi);

        assertTrue(reasons.contains("CREDIT_SCORE_TOO_LOW"));
    }

    @Test
    void shouldRejectWhenAgePlusTenureExceedsLimit() {
        LoanApplicationRequest request = buildRequest(58, 720, 75000, 120);
        BigDecimal emi = new BigDecimal("10000");

        List<String> reasons = eligibilityService.evaluate(request, emi);

        assertTrue(reasons.contains("AGE_TENURE_LIMIT_EXCEEDED"));
    }

    @Test
    void shouldRejectWhenEmiExceedsSixtyPercentOfIncome() {
        LoanApplicationRequest request = buildRequest(30, 720, 20000, 36);
        BigDecimal emi = new BigDecimal("13000");

        List<String> reasons = eligibilityService.evaluate(request, emi);

        assertTrue(reasons.contains("EMI_EXCEEDS_60_PERCENT"));
    }

    @Test
    void shouldReturnNoReasonsForEligibleApplication() {
        LoanApplicationRequest request = buildRequest(30, 720, 75000, 36);
        BigDecimal emi = new BigDecimal("16000");

        List<String> reasons = eligibilityService.evaluate(request, emi);

        assertTrue(reasons.isEmpty());
    }

    @Test
    void shouldRejectWhenEmiBetween50And60PercentOfIncome() {
        LoanApplicationRequest request = buildRequest(30, 720, 75000, 36);
        BigDecimal emi = new BigDecimal("40000");

        List<String> reasons = eligibilityService.evaluate(request, emi);

        assertTrue(reasons.contains("EMI_EXCEEDS_50_PERCENT"));
    }

    @Test
    void shouldCollectMultipleRejectionReasons() {
        LoanApplicationRequest request = buildRequest(58, 550, 20000, 120);
        BigDecimal emi = new BigDecimal("15000");

        List<String> reasons = eligibilityService.evaluate(request, emi);

        assertTrue(reasons.contains("CREDIT_SCORE_TOO_LOW"));
        assertTrue(reasons.contains("AGE_TENURE_LIMIT_EXCEEDED"));
        assertTrue(reasons.contains("EMI_EXCEEDS_60_PERCENT"));
    }
}