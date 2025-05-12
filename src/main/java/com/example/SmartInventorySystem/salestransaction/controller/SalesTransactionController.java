package com.example.SmartInventorySystem.salestransaction.controller;


import com.example.SmartInventorySystem.salestransaction.dto.SalesTransactionDTO;
import com.example.SmartInventorySystem.salestransaction.entity.SalesTransaction;
import com.example.SmartInventorySystem.salestransaction.service.SalesTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        System.out.println(status);
        return salesTransactionService.getTransactionsByStatus(status);
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

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        salesTransactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
