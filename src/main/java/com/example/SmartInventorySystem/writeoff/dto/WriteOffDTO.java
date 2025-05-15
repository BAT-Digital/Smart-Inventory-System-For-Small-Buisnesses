package com.example.SmartInventorySystem.writeoff.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WriteOffDTO {
    private Long batchItemId;
    private BigDecimal quantity;
    private String reason;
}
