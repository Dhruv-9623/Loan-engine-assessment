package com.rbih_assessmenmt.loanengine.dto.response;

import com.rbih_assessmenmt.loanengine.domain.enums.ApplicationStatus;
import com.rbih_assessmenmt.loanengine.domain.enums.Riskband;

import java.util.List;
import java.util.UUID;

public class LoanApplicationResponse {

    private UUID applicationId;
    private ApplicationStatus status;
    private Riskband Riskband;
    private OfferResponse offer;
    private List<String> rejectionReasons;

    public static LoanApplicationResponse approved(UUID id, Riskband Riskband, OfferResponse offer) {
        LoanApplicationResponse r = new LoanApplicationResponse();
        r.applicationId = id;
        r.status = ApplicationStatus.APPROVED;
        r.Riskband = Riskband;
        r.offer = offer;
        return r;
    }

    public static LoanApplicationResponse rejected(UUID id, List<String> reasons) {
        LoanApplicationResponse r = new LoanApplicationResponse();
        r.applicationId = id;
        r.status = ApplicationStatus.REJECTED;
        r.Riskband = null;
        r.rejectionReasons = reasons;
        return r;
    }

    public UUID getApplicationId() { return applicationId; }
    public ApplicationStatus getStatus() { return status; }
    public Riskband getRiskband() { return Riskband; }
    public OfferResponse getOffer() { return offer; }
    public List<String> getRejectionReasons() { return rejectionReasons; }
}