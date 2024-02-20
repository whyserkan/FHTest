package com.example.FHTest.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionListRequestDto {
    private LocalDate fromDate;
    private LocalDate toDate;
    private String status;
    private String operation;
    private Integer merchantId;
    private Integer acquirerId;
    private String paymentMethod;
    private String errorCode;
    private String filterField;
    private String filterValue;
    private Integer page;
}
