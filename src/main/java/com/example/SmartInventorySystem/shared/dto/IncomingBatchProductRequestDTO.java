package com.example.SmartInventorySystem.shared.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class IncomingBatchProductRequestDTO {
    private Long categoryId;
    private String productName;
    private String barcode;
    private Boolean isPerishable;
    private Boolean isComposite;
    private String unitOfMeasure;
    private Long supplierId;
    private String description;
    private BigDecimal threshold;
    private BigDecimal price;
    private BigDecimal volume;
}
