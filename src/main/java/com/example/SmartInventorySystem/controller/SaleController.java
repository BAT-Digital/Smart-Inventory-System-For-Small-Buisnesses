package com.example.SmartInventorySystem.controller;

import com.example.SmartInventorySystem.dto.ProductRequestDTO;
import com.example.SmartInventorySystem.model.SalesTransaction;
import com.example.SmartInventorySystem.service.SaleService;
import com.example.SmartInventorySystem.service.crud.SalesTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/process-sell-transaction/{id}")
    public ResponseEntity<String> processSale(@PathVariable Long id, @RequestBody List<ProductRequestDTO> productRequestDTOS) {
        String result = saleService.processSellTransaction(id, productRequestDTOS);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/new-check")
    public ResponseEntity<SalesTransaction> createNewCheck(@RequestParam String credentials) {
        SalesTransaction salesTransaction = saleService.createNewCheck(credentials);
        return new ResponseEntity<>(salesTransaction, HttpStatus.CREATED);
    }

    @PostMapping("/cancel-check/{id}")
    public ResponseEntity<String> cancelCheck(@PathVariable Long id) {
        String result = saleService.cancelCheck(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
