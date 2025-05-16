package com.example.SmartInventorySystem.batcharrivalitem.dto;

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

    // Custom setters to handle number conversion
    public void setQuantityReceived(Object value) {
        if (value instanceof Number) {
            this.quantityReceived = new BigDecimal(value.toString());
        } else if (value instanceof String) {
            this.quantityReceived = new BigDecimal((String) value);
        }
    }

    public void setUnitCost(Object value) {
        if (value instanceof Number) {
            this.unitCost = new BigDecimal(value.toString());
        } else if (value instanceof String) {
            this.unitCost = new BigDecimal((String) value);
        }
    }
}
