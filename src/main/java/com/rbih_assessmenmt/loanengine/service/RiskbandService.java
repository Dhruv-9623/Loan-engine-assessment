package com.rbih_assessmenmt.loanengine.service;

import com.rbih_assessmenmt.loanengine.domain.enums.Riskband;
import org.springframework.stereotype.Service;

@Service
public class RiskbandService {

    public Riskband classify(int creditScore) {
        if (creditScore >= 750) return Riskband.LOW;
        if (creditScore >= 650) return Riskband.MEDIUM;
        return Riskband.HIGH;
    }
}