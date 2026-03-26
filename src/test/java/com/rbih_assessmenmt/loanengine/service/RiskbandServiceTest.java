package com.rbih_assessmenmt.loanengine.service;

import com.rbih_assessmenmt.loanengine.domain.enums.Riskband;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RiskbandServiceTest {

    private RiskbandService RiskbandService;

    @BeforeEach
    void setUp() {
        RiskbandService = new RiskbandService();
    }

    @Test
    void shouldReturnLowForScoreAbove750() {
        assertEquals(Riskband.LOW, RiskbandService.classify(780));
        assertEquals(Riskband.LOW, RiskbandService.classify(750));
    }

    @Test
    void shouldReturnMediumForScoreBetween650And749() {
        assertEquals(Riskband.MEDIUM, RiskbandService.classify(700));
        assertEquals(Riskband.MEDIUM, RiskbandService.classify(650));
        assertEquals(Riskband.MEDIUM, RiskbandService.classify(749));
    }

    @Test
    void shouldReturnHighForScoreBetween600And649() {
        assertEquals(Riskband.HIGH, RiskbandService.classify(600));
        assertEquals(Riskband.HIGH, RiskbandService.classify(649));
    }
}