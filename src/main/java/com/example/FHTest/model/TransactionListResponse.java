package com.example.FHTest.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TransactionListResponse {
    private int perPage;
    private int currentPage;
    private String nextPageUrl;
    private String prevPageUrl;
    private int from;
    private int to;
    private TransactionResponseData data;

    @Data
    public static class TransactionResponseData {
        private List<Fx> fxInformation;
        private List<Customer> customerInfo;
        private List<Merchant> merchant;
        private List<Ipn> ipn;
        private List<Transaction> transaction;
        private List<Acquirer> acquirer;
        private boolean refundable;
    }

    @Data
    public static class Fx {
        private FxMerchant merchant;
    }

    @Data
    public static class FxMerchant {
        private Integer originalAmount;
        private String originalCurrency;
    }

    @Data
    public static class Customer {
        private String number;
        private String email;
        private String billingFirstName;
        private String billingLastName;
    }

    @Data
    public static class Merchant {
        private Integer originalAmount;
        private String originalCurrency;
    }

    @Data
    public static class Ipn {
        private boolean received;
    }

    @Data
    public static class Transaction {
        private TransactionMerchant merchant;
    }

    @Data
    public static class TransactionMerchant {
        private String referenceNo;
        private String status;
        private String operation;
        private String message;
        private LocalDateTime createdAt;
        private String transactionId;
    }


    @Data
    public static class Acquirer {
        private Integer id;
        private String name;
        private String code;
        private String type;
    }
}
