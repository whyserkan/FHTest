package com.example.FHTest.service;

import jakarta.security.auth.message.AuthException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Slf4j
@Service
public class MerchantAuthService {
    private final WebClient.Builder webClientBuilder;
    private final int tokenExpireInMinutes;
    private final String loginPath;
    private LocalDateTime tokenFetched;
    private final String password;
    private final String email;
    private AuthUser AUTH_USER;
    private String token;

    @Autowired
    public MerchantAuthService(WebClient.Builder webClientBuilder,
                               @Value("${reporting.login.path}") String loginPath,
                               @Value("${reporting.token.expire.in.minutes}") int tokenExpireInMinutes,
                               @Value("${reporting.api.login.email}") String email,
                               @Value("${reporting.api.login.password}") String password) {
        this.webClientBuilder = webClientBuilder;
        this.loginPath = loginPath;
        this.tokenExpireInMinutes = tokenExpireInMinutes;
        this.email = email;
        this.password = password;
    }

    public String getToken() throws AuthException {
        if (token==null || tokenFetched==null || now().minusMinutes(tokenExpireInMinutes).isAfter(tokenFetched)) {
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

    private void fetchToken() throws AuthException {
        TokenFromAuthService tokenFromAuthService =
                webClientBuilder.build()
                .post()
                .uri(loginPath)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(getAuthUser()), AuthUser.class)
                .retrieve()
                .bodyToMono(TokenFromAuthService.class)
                        .doOnError(this::logTokenError)
                        .block();

        setTokenValue(tokenFromAuthService);
    }

    private void logTokenError(Throwable e) {
        log.error("Error on fetching token", e);
    }

    private void setTokenValue(TokenFromAuthService tokenFromAuthService) throws AuthException {
        if (tokenFromAuthService==null || !tokenFromAuthService.getStatus().equals("APPROVED")) {
            throw new AuthException("Credentials are not correct");
        }
        token = tokenFromAuthService.getToken();
        tokenFetched = LocalDateTime.now();
    }

    @Getter
    @AllArgsConstructor
    private static class AuthUser {
        private String password;
        private String email;
    }

    @Data
    private static class TokenFromAuthService {
        private String token;
        private String status;
    }
}
