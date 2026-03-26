package com.rbih_assessmenmt.loanengine.dto.request;
import com.rbih_assessmenmt.loanengine.domain.enums.LoanPurpose;
import jakarta.validation.constraints.*;

public class LoanRequestDto {

    @DecimalMin(value = "10000") @DecimalMax(value = "5000000")
    private double amount;

    @Min(6) @Max(360)
    private int tenureMonths;

    @NotNull
    private LoanPurpose purpose;

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public int getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }

    public LoanPurpose getPurpose() { return purpose; }
    public void setPurpose(LoanPurpose purpose) { this.purpose = purpose; }
}