package com.example.FHTest;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableEncryptableProperties
public class FhTestApplication {
	@Value("${reporting.base.url}")
	private String reportingUrl;

	public static void main(String[] args) {
		SpringApplication.run(FhTestApplication.class, args);
	}

	@Bean
	public WebClient.Builder webClientBuilder() {
		return WebClient.builder().baseUrl(reportingUrl);
	}
}
