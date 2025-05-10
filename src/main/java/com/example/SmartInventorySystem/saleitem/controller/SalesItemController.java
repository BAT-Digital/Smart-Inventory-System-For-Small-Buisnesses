package com.example.SmartInventorySystem.saleitem.controller;

import com.example.SmartInventorySystem.saleitem.service.SalesItemService;
import com.example.SmartInventorySystem.saleitem.dto.SalesItemDTO;
import com.example.SmartInventorySystem.saleitem.entity.SalesItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales-items")
public class SalesItemController {

    private final SalesItemService salesItemService;

    public SalesItemController(SalesItemService salesItemService) {
        this.salesItemService = salesItemService;
    }

    @GetMapping
    public List<SalesItem> getAllSalesItems() {
        return salesItemService.getAllSalesItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesItem> getSalesItemById(@PathVariable Long id) {
        Optional<SalesItem> salesItem = salesItemService.getSalesItemById(id);
        return salesItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // returns all sales items by transaction id
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<List<SalesItem>> getSalesItemsByTransactionId(@PathVariable Long transactionId) {
        List<SalesItem> salesItems = salesItemService.getSalesItemsByTransactionId(transactionId);
        return ResponseEntity.ok(salesItems);
    }

    @PostMapping("/create-by-dto")
    public ResponseEntity<SalesItem> createSalesItemByDTO(@RequestBody SalesItemDTO salesItemDTO) {
        SalesItem createdSalesItem = salesItemService.createSalesItemByDTO(salesItemDTO);
        return new ResponseEntity<>(createdSalesItem, HttpStatus.CREATED);
    }

    @PostMapping("/create-by-entity")
    public SalesItem createSalesItem(@RequestBody SalesItem salesItem) {
        return salesItemService.createSalesItem(salesItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesItem> updateSalesItem(@PathVariable Long id, @RequestBody SalesItem salesItem) {
        SalesItem updatedItem = salesItemService.updateSalesItem(id, salesItem);
        return updatedItem != null ? ResponseEntity.ok(updatedItem) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalesItem(@PathVariable Long id) {
        salesItemService.deleteSalesItem(id);
        return ResponseEntity.noContent().build();
    }
}
