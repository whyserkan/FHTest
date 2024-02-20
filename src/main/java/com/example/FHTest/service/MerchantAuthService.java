package com.example.FHTest.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class MerchantAuthService {

    private final WebClient.Builder webClientBuilder;

    @Value("${reporting.token.expire.in.minutes}")
    private int tokenExpireInMinutes;

    @Value("${reporting.login.path}")
    private String loginPath;

    private LocalDateTime tokenFetched;

    @Value("${reporting.api.login.password}")
    private String password;

    @Value("${reporting.api.login.email}")
    private String email;

    private AuthUser AUTH_USER;

    private String token;

    @Autowired
    public MerchantAuthService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;

    }

    public String getToken() {
        if (token==null || tokenFetched==null || LocalDateTime.now().minusMinutes(tokenExpireInMinutes).isAfter(tokenFetched)) {
            fetchToken();
        }
        return token;
    }

    private AuthUser getAuthUser(){
        if (AUTH_USER == null){
            AUTH_USER = new AuthUser(password, email);
        }
        return AUTH_USER;
    }

    private void fetchToken() {
        Mono<TokenFromAuthService> tokenFromAuthServiceMono = webClientBuilder.build()
                .post()
                .uri(loginPath)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(getAuthUser()), AuthUser.class)
                .retrieve()
                .bodyToMono(TokenFromAuthService.class);

        tokenFromAuthServiceMono.doOnError(throwable -> throwable.printStackTrace());

        TokenFromAuthService tokenFromAuthService = tokenFromAuthServiceMono.block();

        if (tokenFromAuthService==null || !tokenFromAuthService.getStatus().equals("APPROVED")) {
            throw new RuntimeException("Error on fetching token");
        }
        token = tokenFromAuthService.getToken();
        tokenFetched = LocalDateTime.now();
    }

    @Getter
    @AllArgsConstructor
    public static class AuthUser {
        private String password;
        private String email;
    }

    @Data
    private static class TokenFromAuthService {
        private String token;
        private String status;
    }
}
