package com.example.FHTest.service;

import com.example.FHTest.model.TransactionListResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import jakarta.security.auth.message.AuthException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(properties = {"jasypt.encryptor.password=secret"})
@EnableWireMock({
        @ConfigureWireMock(name = "transaction-service", property = "transaction-client.url")
})
class TransactionServiceTest {

    private TransactionService transactionService;

    @Mock
    private MerchantAuthService merchantAuthService;

    @InjectWireMock("transaction-service")
    private WireMockServer wiremock;

    @Autowired
    private Environment env;

    private final static String LIST_PATH = "/path";

    @Test
    void testIfListServiceReturnsCustomer_groupByCustomerSurnameShouldGroup() throws AuthException {
        Mockito.when(merchantAuthService.getToken()).thenReturn("token");

        var webClintBuilder = WebClient.builder().baseUrl(env.getProperty("transaction-client.url"));

        wiremock.stubFor(post(LIST_PATH).willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody("""
                             { "data" : 
                                {"customerInfo" : 
                                    [
                                     {"billingLastName": "surname11"}, 
                                     {"billingLastName": "surname11"},
                                     {"billingLastName": "surname22"}
                                    ] 
                                }
                              }
                          """)
        ));

        transactionService = new TransactionService(merchantAuthService, webClintBuilder, LIST_PATH, "", "");
        List<TransactionListResponse.Customer> customers = transactionService.fetchCustomersBy("surname11");
        assertEquals(customers.size(), 2);
    }

    @Test
    void testIfListServiceReturnsEmptyData_groupByCustomerSurnameShouldReturnEmptyList() throws AuthException {
        Mockito.when(merchantAuthService.getToken()).thenReturn("token");

        var webClintBuilder = WebClient.builder().baseUrl(env.getProperty("transaction-client.url"));

        wiremock.stubFor(post(LIST_PATH).willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody("""
                             { "data" :{}}
                          """)
        ));

        transactionService = new TransactionService(merchantAuthService, webClintBuilder, LIST_PATH, "", "");
        List<TransactionListResponse.Customer> customers = transactionService.fetchCustomersBy("surname11");
        assertEquals(customers.size(), 0);
    }
}