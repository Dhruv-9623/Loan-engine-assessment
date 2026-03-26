package com.rbih_assessmenmt.loanengine.dto.response;

import java.math.BigDecimal;

public class OfferResponse {

    private BigDecimal interestRate;
    private int tenureMonths;
    private BigDecimal emi;
    private BigDecimal totalPayable;

    public OfferResponse(BigDecimal interestRate, int tenureMonths, BigDecimal emi, BigDecimal totalPayable) {
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
        this.emi = emi;
        this.totalPayable = totalPayable;
    }

    public BigDecimal getInterestRate() { return interestRate; }
    public int getTenureMonths() { return tenureMonths; }
    public BigDecimal getEmi() { return emi; }
    public BigDecimal getTotalPayable() { return totalPayable; }
}