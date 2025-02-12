package com.example.SmartInventorySystem.service.interfaces;


import com.example.SmartInventorySystem.model.InventoryTransaction;

import java.util.List;

public interface InventoryTransactionService {
    InventoryTransaction createTransaction(InventoryTransaction transaction);
    InventoryTransaction getTransactionById(Long id);
    List<InventoryTransaction> getAllTransactions();
    // Additional methods for report generation can be added here
}