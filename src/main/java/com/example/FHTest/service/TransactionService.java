package com.example.FHTest.service;

import com.example.FHTest.model.*;
import jakarta.security.auth.message.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
public class TransactionService {
    private final String transactionListPath;
    private final String transactionReportPath;
    private final String transactionFetchPath;
    private final MerchantAuthService merchantAuthService;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public TransactionService(MerchantAuthService merchantAuthService,
                              WebClient.Builder webClientBuilder,
                              @Value("${reporting.transaction.list.path}") String transactionListPath,
                              @Value("${reporting.transaction.report.path}") String transactionReportPath,
                              @Value("${reporting.transaction.get.path}") String transactionFetchPath) {
        this.merchantAuthService = merchantAuthService;
        this.webClientBuilder = webClientBuilder;
        this.transactionListPath = transactionListPath;
        this.transactionReportPath = transactionReportPath;
        this.transactionFetchPath = transactionFetchPath;
    }

    public TransactionListResponse search(TransactionListRequestDto transactionListRequestDto) throws AuthException {
        return postAndFetchWithToken(transactionListRequestDto, TransactionListRequestDto.class, TransactionListResponse.class, transactionListPath).block();
    }

    public List<TransactionListResponse.Customer> fetchCustomersBy(String customerSurname) throws AuthException {
        var response =
                postAndFetchWithToken(new TransactionListRequestDto(), TransactionListRequestDto.class, TransactionListResponse.class, transactionListPath).block();

        if (response!=null && response.getData()!=null && response.getData().getCustomerInfo()!=null) {
            return response.getData()
                    .getCustomerInfo().stream()
                    .filter(customer -> customer.getBillingLastName().equals(customerSurname))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public TransactionReportResponse fetchReport(TransactionReportRequestDto transactionReportRequestDto) throws AuthException {
        return postAndFetchWithToken(transactionReportRequestDto, TransactionReportRequestDto.class, TransactionReportResponse.class, transactionReportPath).block();
    }

    public TransactionFetchResponse getTransaction(TransactionFetchRequestDto transactionFetchRequestDto) throws AuthException {
        return postAndFetchWithToken(transactionFetchRequestDto, TransactionFetchRequestDto.class, TransactionFetchResponse.class, transactionFetchPath).block();
    }
    
    private <T, D> Mono<T> postAndFetchWithToken(D d, Class<D> dClass, Class<T> tClass , String path) throws AuthException {
        final String token = merchantAuthService.getToken();
        return webClientBuilder.build()
                .post()
                .uri(path)
                .headers(httpHeaders -> httpHeaders.add(AUTHORIZATION, token))
                .body(Mono.just(d), dClass)
                .retrieve()
                .bodyToMono(tClass);
    }
}
