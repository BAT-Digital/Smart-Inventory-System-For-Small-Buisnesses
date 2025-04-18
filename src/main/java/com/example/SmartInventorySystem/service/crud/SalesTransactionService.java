package com.example.SmartInventorySystem.service.crud;

import com.example.SmartInventorySystem.dto.SalesTransactionDTO;
import com.example.SmartInventorySystem.model.SalesTransaction;
import com.example.SmartInventorySystem.repository.crud.SalesTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalesTransactionService {

    private final SalesTransactionRepository salesTransactionRepository;

    public SalesTransactionService(SalesTransactionRepository salesTransactionRepository) {
        this.salesTransactionRepository = salesTransactionRepository;
    }

    public List<SalesTransaction> getAllTransactions() {
        return salesTransactionRepository.findAll();
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

    public void deleteTransaction(Long id) {
        salesTransactionRepository.deleteById(id);
    }
}
