package com.rbih_assessmenmt.loanengine.service;

import com.rbih_assessmenmt.loanengine.domain.enums.EmploymentType;
import com.rbih_assessmenmt.loanengine.domain.enums.Riskband;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class InterestRateService {

    private static final BigDecimal BASE_RATE = new BigDecimal("12.0");

    public BigDecimal calculate(Riskband Riskband, EmploymentType employmentType, BigDecimal loanAmount) {
        BigDecimal rate = BASE_RATE;
        rate = rate.add(riskPremium(Riskband));
        rate = rate.add(employmentPremium(employmentType));
        rate = rate.add(loanSizePremium(loanAmount));
        return rate;
    }

    private BigDecimal riskPremium(Riskband Riskband) {
        return switch (Riskband) {
            case LOW -> BigDecimal.ZERO;
            case MEDIUM -> new BigDecimal("1.5");
            case HIGH -> new BigDecimal("3.0");
        };
    }

    private BigDecimal employmentPremium(EmploymentType employmentType) {
        return employmentType == EmploymentType.SELF_EMPLOYED
                ? new BigDecimal("1.0")
                : BigDecimal.ZERO;
    }

    private BigDecimal loanSizePremium(BigDecimal loanAmount) {
        return loanAmount.compareTo(new BigDecimal("1000000")) > 0
                ? new BigDecimal("0.5")
                : BigDecimal.ZERO;
    }
}