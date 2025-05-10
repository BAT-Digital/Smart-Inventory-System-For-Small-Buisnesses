package com.example.SmartInventorySystem.shared.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SaleRecord {
    private LocalDate date;
    private Long productId;
    private Long quantity;

    public SaleRecord(LocalDate date, Long productId, Long quantity) {
        this.date = date;
        this.productId = productId;
        this.quantity = quantity;
    }

}
