package com.example.FHTest.model;

import lombok.Data;

@Data
public class TransactionReportResponse {
    private String status;
    private Response response;

    @Data
    public static class Response {
        private int count;
        private int total;
        private String currency;
    }
}
