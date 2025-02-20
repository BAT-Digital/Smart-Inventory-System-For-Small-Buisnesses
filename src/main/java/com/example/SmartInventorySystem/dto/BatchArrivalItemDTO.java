package com.example.SmartInventorySystem.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BatchArrivalItemDTO {
    private Long batchArrivalId;
    private Long productId;
    private BigDecimal quantityReceived;
    private LocalDate expiryDate;
    private BigDecimal unitCost;
}
