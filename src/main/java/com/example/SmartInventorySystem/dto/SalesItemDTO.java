package com.example.SmartInventorySystem.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesItemDTO {
    private Long transactionId;
    private Long productId;
    private BigDecimal quantity;
}
