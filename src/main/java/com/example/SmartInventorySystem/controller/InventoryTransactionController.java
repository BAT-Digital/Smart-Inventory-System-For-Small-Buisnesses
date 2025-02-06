package com.example.SmartInventorySystem.controller;


import com.example.SmartInventorySystem.model.InventoryTransaction;
import com.example.SmartInventorySystem.service.interfaces.InventoryTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class InventoryTransactionController {

    @Autowired
    private InventoryTransactionService transactionService;

    // Endpoint to create a new transaction (e.g., a sale or a write-off)
    @PostMapping
    public ResponseEntity<InventoryTransaction> createTransaction(@RequestBody InventoryTransaction transaction) {
        InventoryTransaction createdTransaction = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    // Endpoint to retrieve a single transaction by its ID
    @GetMapping("/{id}")
    public ResponseEntity<InventoryTransaction> getTransactionById(@PathVariable("id") Long id) {
        InventoryTransaction transaction = transactionService.getTransactionById(id);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    // Endpoint to retrieve all transactions
    @GetMapping
    public ResponseEntity<List<InventoryTransaction>> getAllTransactions() {
        List<InventoryTransaction> transactions = transactionService.getAllTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    // (Optional) Create endpoints for report generation, for example:
    // @GetMapping("/reports/sales")
    // public ResponseEntity<SalesReport> getSalesReport(@RequestParam Map<String, String> params) {
    //     // Implement report logic here...
    // }
}