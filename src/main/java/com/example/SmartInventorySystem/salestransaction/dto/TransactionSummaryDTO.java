package com.example.SmartInventorySystem.salestransaction.dto;


import com.example.SmartInventorySystem.salestransaction.entity.SalesTransaction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class TransactionSummaryDTO {
    private BigDecimal total;
    private List<SalesTransaction> transactions;
}