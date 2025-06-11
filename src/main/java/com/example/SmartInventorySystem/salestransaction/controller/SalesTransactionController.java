package com.example.SmartInventorySystem.salestransaction.controller;


import com.example.SmartInventorySystem.salestransaction.dto.DateRangeRequest;
import com.example.SmartInventorySystem.salestransaction.dto.SalesTransactionDTO;
import com.example.SmartInventorySystem.salestransaction.dto.TransactionSummaryDTO;
import com.example.SmartInventorySystem.salestransaction.entity.SalesTransaction;
import com.example.SmartInventorySystem.salestransaction.service.SalesTransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales-transactions")
public class SalesTransactionController {

    private final SalesTransactionService salesTransactionService;

    public SalesTransactionController(SalesTransactionService salesTransactionService) {
        this.salesTransactionService = salesTransactionService;
    }

    @GetMapping
    public List<SalesTransaction> getAllTransactions() {
        return salesTransactionService.getAllTransactions();
    }

    @GetMapping("/by-status")
    public List<SalesTransaction> getByStatus(@RequestParam String status) {
        return salesTransactionService.getTransactionsByStatus(status);
    }

    @GetMapping("/by-date-range")
    public TransactionSummaryDTO getSalesBetweenDates(@RequestBody DateRangeRequest request) {

        return salesTransactionService.getTransactionsBetweenDates(request.getStartDate(), request.getEndDate());
    }


    @GetMapping("/{id}")
    public ResponseEntity<SalesTransaction> getTransactionById(@PathVariable Long id) {
        Optional<SalesTransaction> transaction = salesTransactionService.getTransactionById(id);
        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create-by-dto")
    public ResponseEntity<SalesTransaction> createSalesTransactionByDto(@RequestBody SalesTransactionDTO salesTransactionDTO) {
        SalesTransaction createdSalesTransaction = salesTransactionService.createSalesTransactionByDto(salesTransactionDTO);
        return new ResponseEntity<>(createdSalesTransaction, HttpStatus.CREATED);
    }

    @PostMapping("/create-by-entity")
    public SalesTransaction createTransaction(@RequestBody SalesTransaction transaction) {
        return salesTransactionService.createTransaction(transaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesTransaction> updateTransaction(@PathVariable Long id, @RequestBody SalesTransaction transaction) {
        SalesTransaction updatedTransaction = salesTransactionService.updateTransaction(id, transaction);
        return updatedTransaction != null ? ResponseEntity.ok(updatedTransaction) : ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        salesTransactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
