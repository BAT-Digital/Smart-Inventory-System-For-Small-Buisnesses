package com.example.SmartInventorySystem.shared.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductRequestDTO {
    private Long userId;
    private String barcode;
    private LocalDate expirationDate;
    private BigDecimal quantity;
}
