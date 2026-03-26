package com.rbih_assessmenmt.loanengine.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class EmiCalculatorServiceTest {

    private EmiCalculatorService emiCalculatorService;

    @BeforeEach
    void setUp() {
        emiCalculatorService = new EmiCalculatorService();
    }

    @Test
    void shouldCalculateCorrectEmiForStandardInput() {
        BigDecimal principal = new BigDecimal("500000");
        BigDecimal annualRate = new BigDecimal("12.0");
        int tenure = 36;

        BigDecimal emi = emiCalculatorService.calculate(principal, annualRate, tenure);

        assertNotNull(emi);
        assertTrue(emi.compareTo(BigDecimal.ZERO) > 0);
        assertEquals(new BigDecimal("16607.15"), emi);
    }

    @Test
    void shouldReturnHigherEmiForShorterTenure() {
        BigDecimal principal = new BigDecimal("500000");
        BigDecimal annualRate = new BigDecimal("12.0");

        BigDecimal emi36 = emiCalculatorService.calculate(principal, annualRate, 36);
        BigDecimal emi60 = emiCalculatorService.calculate(principal, annualRate, 60);

        assertTrue(emi36.compareTo(emi60) > 0);
    }

    @Test
    void shouldReturnHigherEmiForHigherInterestRate() {
        BigDecimal principal = new BigDecimal("500000");
        int tenure = 36;

        BigDecimal emiLow = emiCalculatorService.calculate(principal, new BigDecimal("12.0"), tenure);
        BigDecimal emiHigh = emiCalculatorService.calculate(principal, new BigDecimal("15.0"), tenure);

        assertTrue(emiHigh.compareTo(emiLow) > 0);
    }
}