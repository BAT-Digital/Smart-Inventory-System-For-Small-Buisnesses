package com.example.SmartInventorySystem.dto.analysisdto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailySalesTrendDto {
    private LocalDate date;
    private BigDecimal totalRevenue;
    private Long transactionCount;
    private BigDecimal netProfit;
}