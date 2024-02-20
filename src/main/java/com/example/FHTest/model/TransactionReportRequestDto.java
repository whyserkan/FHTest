package com.example.FHTest.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionReportRequestDto {
    private LocalDate fromDate;
    private LocalDate toDate;
    private Integer merchant;
    private Integer acquirer;
}
