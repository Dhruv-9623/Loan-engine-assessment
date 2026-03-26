package com.rbih_assessmenmt.loanengine.controller;

import com.rbih_assessmenmt.loanengine.dto.request.LoanApplicationRequest;
import com.rbih_assessmenmt.loanengine.dto.response.LoanApplicationResponse;
import com.rbih_assessmenmt.loanengine.service.LoanApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/applications")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    public LoanApplicationController(LoanApplicationService loanApplicationService) {
        this.loanApplicationService = loanApplicationService;
    }

    @PostMapping
    public ResponseEntity<LoanApplicationResponse> apply(@Valid @RequestBody LoanApplicationRequest request) {
        LoanApplicationResponse response = loanApplicationService.process(request);
        return ResponseEntity.ok(response);
    }
}