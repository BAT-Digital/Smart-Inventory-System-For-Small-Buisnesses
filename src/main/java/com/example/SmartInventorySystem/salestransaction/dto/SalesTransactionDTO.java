package com.example.SmartInventorySystem.salestransaction.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesTransactionDTO {
    private String credentials;
    private String status;
    private BigDecimal totalAmount;
}
