package com.rbih_assessmenmt.loanengine.domain.entity;
import com.rbih_assessmenmt.loanengine.domain.enums.LoanPurpose;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class LoanRequest {

    private double amount;
    private int tenureMonths;

    @Enumerated(EnumType.STRING)
    private LoanPurpose purpose;

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public int getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }

    public LoanPurpose getPurpose() { return purpose; }
    public void setPurpose(LoanPurpose purpose) { this.purpose = purpose; }
}
