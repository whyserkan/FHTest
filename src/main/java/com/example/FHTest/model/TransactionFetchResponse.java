package com.example.FHTest.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionFetchResponse {
    private Fx fx;
    private Customer customerInfo;
    private Merchant merchant;
    private MerchantTransaction merchantTransactions;
    private AcquirerTransaction acquirerTransactions;

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
        private int id;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime deletedAt;
        private String number;
        private String expiryMonth;
        private String expiryYear;
        private String startMonth;
        private String startYear;
        private String issueNumber;
        private String email;
        private LocalDateTime birthday;
        private String gender;
        private String billingTitle;
        private String billingFirstName;
        private String billingLastName;
        private String billingCompany;
        private String billingAddress1;
        private String billingAddress2;
        private String billingCity;
        private String billingPostcode;
        private String billingState;
        private String billingCountry;
        private String billingPhone;
        private String billingFax;
        private String shippingTitle;
        private String shippingFirstName;
        private String shippingLastName;
        private String shippingCompany;
        private String shippingAddress1;
        private String shippingAddress2;
        private String shippingCity;
        private String shippingPostcode;
        private String shippingState;
        private String shippingCountry;
        private String shippingPhone;
        private String shippingFax;
    }

    @Data
    public static class Merchant {
        private String name;
    }

    @Data
    public static class Ipn {
        private boolean received;
    }

    @Data
    public static class MerchantTransaction {
        private TransactionMerchant merchant;
    }

    @Data
    public static class TransactionMerchant {
        private String referenceNo;
        private Integer merchantId;
        private String status;
        private String channel;
        private String customData;
        private String chainId;
        private Integer agentInfoId;
        private String operation;
        private Integer fxTransactionId;
        private LocalDateTime updatedAt;
        private LocalDateTime createdAt;
        private Integer id;
        private Integer acquirerTransactionId;
        private String code;
        private String message;
        private String transactionId;
        private Agent agent;
    }


    @Data
    public static class Agent {
        private Integer id;
        private String customerIp;
        private String customerUserAgent;
        private String merchantIp;
    }

    @Data
    public static class AcquirerTransaction {
        private String name;
        private String code;
    }
}
