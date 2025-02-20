package com.example.SmartInventorySystem.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BatchArrivalDTO {
    private Long supplierId;
    private String notes;
    private Long addedById;
}
