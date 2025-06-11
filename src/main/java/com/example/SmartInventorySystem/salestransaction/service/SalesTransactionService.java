package com.example.SmartInventorySystem.salestransaction.service;

import com.example.SmartInventorySystem.saleitem.repository.SalesItemRepository;
import com.example.SmartInventorySystem.salestransaction.dto.SalesTransactionDTO;

import com.example.SmartInventorySystem.salestransaction.dto.TransactionSummaryDTO;
import com.example.SmartInventorySystem.salestransaction.entity.SalesTransaction;
import com.example.SmartInventorySystem.salestransaction.repository.SalesTransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public TransactionSummaryDTO getTransactionsBetweenDates(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
       List<SalesTransaction> transactions = salesTransactionRepository.findByTransactionDateBetweenAndStatusOrderByTransactionDateDesc(startDateTime, endDateTime,"COMPLETED");
        BigDecimal total = transactions.stream()
                .map(SalesTransaction::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new TransactionSummaryDTO(total, transactions);
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
