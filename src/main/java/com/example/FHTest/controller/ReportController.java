package com.example.FHTest.controller;

import com.example.FHTest.model.*;
import com.example.FHTest.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Mono<TransactionListResponse> getSearchTransaction() {
        TransactionListRequestDto transactionListRequestDto = new TransactionListRequestDto();
        return transactionService.search(transactionListRequestDto);
    }

    @GetMapping("/transaction/report")
    public Mono<TransactionReportResponse> getTransactionReport() {
        TransactionReportRequestDto transactionReportRequestDto = new TransactionReportRequestDto();
        return transactionService.fetchReport(transactionReportRequestDto);
    }

    @GetMapping("/transaction/{id}")
    public Mono<TransactionFetchResponse> getTransaction(@PathVariable String id) {
        TransactionFetchRequestDto transactionFetchRequestDto = new TransactionFetchRequestDto();
        transactionFetchRequestDto.setTransactionId(id);
        return transactionService.getTransaction(transactionFetchRequestDto);
    }
}
