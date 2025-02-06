package com.example.SmartInventorySystem.service;


import com.example.SmartInventorySystem.model.InventoryTransaction;
import com.example.SmartInventorySystem.repository.InventoryTransactionRepository;
import com.example.SmartInventorySystem.service.interfaces.InventoryTransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InventoryTransactionServiceImpl implements InventoryTransactionService {

    @Autowired
    private InventoryTransactionRepository transactionRepository;

    @Override
    public InventoryTransaction createTransaction(InventoryTransaction transaction) {
        // Business logic could include:
        // - Validating transaction details
        // - Calculating the totalAmount based on its transaction lines (if needed)
        return transactionRepository.save(transaction);
    }

    @Override
    public InventoryTransaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
    }

    @Override
    public List<InventoryTransaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}