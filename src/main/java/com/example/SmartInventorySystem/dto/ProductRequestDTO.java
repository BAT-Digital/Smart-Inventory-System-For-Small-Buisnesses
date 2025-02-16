package com.example.SmartInventorySystem.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductRequestDTO {
    private String barcode;
    private LocalDate expirationDate;
    private BigDecimal quantity;
}
