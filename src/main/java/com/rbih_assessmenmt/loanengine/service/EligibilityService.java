package com.rbih_assessmenmt.loanengine.service;

import com.rbih_assessmenmt.loanengine.dto.request.LoanApplicationRequest;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class EligibilityService {

    public List<String> evaluate(LoanApplicationRequest request, BigDecimal emi) {
        List<String> reasons = new ArrayList<>();

        int creditScore = request.getApplicant().getCreditScore();
        int age = request.getApplicant().getAge();
        int tenureMonths = request.getLoan().getTenureMonths();
        BigDecimal monthlyIncome = BigDecimal.valueOf(request.getApplicant().getMonthlyIncome());

        if (creditScore < 600) {
            reasons.add("CREDIT_SCORE_TOO_LOW");
        }

        double tenureYears = tenureMonths / 12.0;
        if (age + tenureYears > 65) {
            reasons.add("AGE_TENURE_LIMIT_EXCEEDED");
        }

        BigDecimal sixtyPercent = monthlyIncome.multiply(new BigDecimal("0.60")).setScale(2, RoundingMode.HALF_UP);
        if (emi.compareTo(sixtyPercent) > 0) {
            reasons.add("EMI_EXCEEDS_60_PERCENT");
        }

        return reasons;
    }

    public boolean isEmiWithinOfferLimit(BigDecimal emi, BigDecimal monthlyIncome) {
        BigDecimal fiftyPercent = monthlyIncome.multiply(new BigDecimal("0.50")).setScale(2, RoundingMode.HALF_UP);
        return emi.compareTo(fiftyPercent) <= 0;
    }
}