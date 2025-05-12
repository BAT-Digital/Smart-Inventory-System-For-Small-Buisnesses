package com.example.SmartInventorySystem.saleitem.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SalesItemDTO {
    private Long transactionId;
    private Long productId;
    private LocalDate expiryDate;
    private BigDecimal quantity;
}
