package com.example.SmartInventorySystem.salestransaction.service;

import com.example.SmartInventorySystem.saleitem.repository.SalesItemRepository;
import com.example.SmartInventorySystem.salestransaction.dto.SalesTransactionDTO;

import com.example.SmartInventorySystem.salestransaction.entity.SalesTransaction;
import com.example.SmartInventorySystem.salestransaction.repository.SalesTransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalesTransactionService {

    private final SalesTransactionRepository salesTransactionRepository;

    private final SalesItemRepository salesItemRepository;

    public SalesTransactionService(SalesTransactionRepository salesTransactionRepository, SalesItemRepository salesItemRepository) {
        this.salesTransactionRepository = salesTransactionRepository;
        this.salesItemRepository = salesItemRepository;
    }

    public List<SalesTransaction> getAllTransactions() {
        return salesTransactionRepository.findAll();
    }

    public List<SalesTransaction> getTransactionsByStatus(String status) {
        return salesTransactionRepository.findByStatus(status);
    }

    public Optional<SalesTransaction> getTransactionById(Long id) {
        return salesTransactionRepository.findById(id);
    }

    public SalesTransaction createTransaction(SalesTransaction transaction) {
        return salesTransactionRepository.save(transaction);
    }

    public SalesTransaction createSalesTransactionByDto(SalesTransactionDTO salesTransactionDTO) {
        SalesTransaction salesTransaction = new SalesTransaction();
        salesTransaction.setCredentials(salesTransactionDTO.getCredentials());
        salesTransaction.setStatus(salesTransactionDTO.getStatus());
        salesTransaction.setTotalAmount(salesTransactionDTO.getTotalAmount());
        return salesTransactionRepository.save(salesTransaction);
    }

    public SalesTransaction updateTransaction(Long id, SalesTransaction updatedTransaction) {
        return salesTransactionRepository.findById(id).map(transaction -> {
            transaction.setTransactionDate(updatedTransaction.getTransactionDate());
            transaction.setTotalAmount(updatedTransaction.getTotalAmount());
            return salesTransactionRepository.save(transaction);
        }).orElse(null);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        salesItemRepository.deleteBySalesTransactionId(id);
        salesTransactionRepository.deleteById(id);
    }
}
