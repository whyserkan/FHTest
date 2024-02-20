package com.example.FHTest.service;

import com.example.FHTest.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TransactionService {
    @Value("${reporting.transaction.list.path}")
    private String transactionListPath;

    @Value("${reporting.transaction.report.path}")
    private String transactionReportPath;

    @Value("${reporting.transaction.get.path}")
    private String transactionFetchPath;

    private final MerchantAuthService merchantAuthService;

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public TransactionService(MerchantAuthService merchantAuthService, WebClient.Builder webClientBuilder) {
        this.merchantAuthService = merchantAuthService;
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<TransactionListResponse> search(TransactionListRequestDto transactionListRequestDto) {
        return webClientBuilder.build()
                .post()
                .uri(transactionListPath)
                .headers(httpHeaders -> httpHeaders.add("Authorization", merchantAuthService.getToken()))
                .body(Mono.just(transactionListRequestDto), TransactionListRequestDto.class)
                .retrieve()
                .bodyToMono(TransactionListResponse.class);
    }

    public Mono<TransactionReportResponse> fetchReport(TransactionReportRequestDto transactionReportRequestDto) {
        return webClientBuilder.build()
                .post()
                .uri(transactionReportPath)
                .headers(httpHeaders -> httpHeaders.add("Authorization", merchantAuthService.getToken()))
                .body(Mono.just(transactionReportRequestDto), TransactionReportRequestDto.class)
                .retrieve()
                .bodyToMono(TransactionReportResponse.class);
    }

    public Mono<TransactionFetchResponse> getTransaction(TransactionFetchRequestDto transactionFetchRequestDto) {
        return webClientBuilder.build()
                .post()
                .uri(transactionFetchPath)
                .headers(httpHeaders -> httpHeaders.add("Authorization", merchantAuthService.getToken()))
                .body(Mono.just(transactionFetchRequestDto), TransactionFetchRequestDto.class)
                .retrieve()
                .bodyToMono(TransactionFetchResponse.class);
    }
}
