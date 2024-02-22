package com.example.FHTest.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import jakarta.security.auth.message.AuthException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = {"jasypt.encryptor.password=secret"})
@EnableWireMock({
        @ConfigureWireMock(name = "auth-service", property = "auth-client.url")
})
public class MerchantAuthServiceTest {
    private MerchantAuthService merchantAuthService;

    @InjectWireMock("auth-service")
    private WireMockServer wiremock;

    private final static String PATH = "/path";

    @Autowired
    private Environment env;
    private final String email = "email";
    private final String password = "pass1";
    private final int expire = 1;

    @Test
    void testIfCredentialsAreNotCorrect_getTokenShouldThrowException() {
        var webClintBuilder = WebClient.builder().baseUrl(env.getProperty("auth-client.url"));

        wiremock.stubFor(post(PATH).willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody("""
                             { "token": "xx", "status": "REJECTED"}
                          """)
        ));

        merchantAuthService = new MerchantAuthService(webClintBuilder, PATH, expire, email, password);
        assertThrows(AuthException.class, () -> merchantAuthService.getToken());
    }

    @Test
    void testIfCredentialsAreCorrect_getTokenShouldGetToken() throws AuthException {
        var webClintBuilder = WebClient.builder().baseUrl(env.getProperty("auth-client.url"));

        wiremock.stubFor(post(PATH).willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody("""
                             { "token": "xx", "status": "APPROVED"}
                          """)
        ));

        merchantAuthService = new MerchantAuthService(webClintBuilder, PATH, expire, email, password);
        assertEquals(merchantAuthService.getToken(), "xx");
    }

    /// there can be more tests about timeout maybe
}