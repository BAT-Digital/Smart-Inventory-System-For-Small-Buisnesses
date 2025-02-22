package com.example.SmartInventorySystem.dto.analysisdto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductProfitDto {
    private Long productId;
    private BigDecimal unitsSold;
    private BigDecimal revenue;
    private BigDecimal averageUnitCost;
    private BigDecimal netProfit;
}

