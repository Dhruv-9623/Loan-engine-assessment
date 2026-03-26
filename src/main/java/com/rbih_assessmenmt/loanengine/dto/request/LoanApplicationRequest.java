package com.rbih_assessmenmt.loanengine.dto.request;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class LoanApplicationRequest {

    @NotNull @Valid
    private ApplicantRequest applicant;

    @NotNull @Valid
    private LoanRequestDto loan;

    public ApplicantRequest getApplicant() { return applicant; }
    public void setApplicant(ApplicantRequest applicant) { this.applicant = applicant; }

    public LoanRequestDto getLoan() { return loan; }
    public void setLoan(LoanRequestDto loan) { this.loan = loan; }
}