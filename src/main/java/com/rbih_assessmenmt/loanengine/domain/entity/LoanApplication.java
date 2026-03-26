package com.rbih_assessmenmt.loanengine.domain.entity;

import com.rbih_assessmenmt.loanengine.domain.enums.ApplicationStatus;
import com.rbih_assessmenmt.loanengine.domain.enums.Riskband;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "loan_applications")
public class LoanApplication {

    @Id
    private UUID id;

    @Embedded
    private Applicant applicant;

    @Embedded
    private LoanRequest loanRequest;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Enumerated(EnumType.STRING)
    private Riskband Riskband;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }

    public Applicant getApplicant() { return applicant; }
    public void setApplicant(Applicant applicant) { this.applicant = applicant; }

    public LoanRequest getLoanRequest() { return loanRequest; }
    public void setLoanRequest(LoanRequest loanRequest) { this.loanRequest = loanRequest; }

    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }

    public Riskband getRiskband() { return Riskband; }
    public void setRiskband(Riskband Riskband) { this.Riskband = Riskband; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}