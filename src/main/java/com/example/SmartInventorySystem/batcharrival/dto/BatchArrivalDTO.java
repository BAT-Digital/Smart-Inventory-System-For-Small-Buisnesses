package com.example.SmartInventorySystem.batcharrival.dto;

import lombok.Data;

@Data
public class BatchArrivalDTO {
    private Long supplierId;
    private String notes;
    private Long addedById;
}
