package com.example.SmartInventorySystem.controller.crud;

import com.example.SmartInventorySystem.model.SalesTransaction;
import com.example.SmartInventorySystem.service.crud.SalesTransactionService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<SalesTransaction> getTransactionById(@PathVariable Long id) {
        Optional<SalesTransaction> transaction = salesTransactionService.getTransactionById(id);
        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
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
