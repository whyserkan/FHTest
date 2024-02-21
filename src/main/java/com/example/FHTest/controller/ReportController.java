package com.example.FHTest.controller;

import com.example.FHTest.model.*;
import com.example.FHTest.service.TransactionService;
import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ReportController {
    private final TransactionService transactionService;

    @Autowired
    public ReportController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transaction/search")
    public Mono<ResponseEntity<TransactionListResponse>> getSearchTransaction() {
        TransactionListRequestDto transactionListRequestDto = new TransactionListRequestDto();
        try {
            return Mono.just(ResponseEntity.ok(transactionService.search(transactionListRequestDto)));
        } catch (AuthException e) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
    }

    @GetMapping("/transaction/report")
    public Mono<ResponseEntity<TransactionReportResponse>> getTransactionReport() {
        TransactionReportRequestDto transactionReportRequestDto = new TransactionReportRequestDto();
        try {
            return Mono.just(ResponseEntity.ok(transactionService.fetchReport(transactionReportRequestDto)));
        } catch (AuthException e) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
    }

    @GetMapping("/transaction/{id}")
    public Mono<ResponseEntity<TransactionFetchResponse>> getTransaction(@PathVariable String id) {
        TransactionFetchRequestDto transactionFetchRequestDto = new TransactionFetchRequestDto();
        transactionFetchRequestDto.setTransactionId(id);

        try {
            return Mono.just(ResponseEntity.ok(transactionService.getTransaction(transactionFetchRequestDto)));
        } catch (AuthException e) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
    }
}
