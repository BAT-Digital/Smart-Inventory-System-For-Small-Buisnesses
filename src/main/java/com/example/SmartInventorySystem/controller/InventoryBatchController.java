package com.example.SmartInventorySystem.controller;

import com.example.SmartInventorySystem.model.InventoryBatch;
import com.example.SmartInventorySystem.service.InventoryBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory-batches")
public class InventoryBatchController {

    @Autowired
    private InventoryBatchService inventoryBatchService;

    @GetMapping
    public List<InventoryBatch> getAllBatches() {
        return inventoryBatchService.getAllBatches();
    }

    @GetMapping("/{batchId}")
    public ResponseEntity<InventoryBatch> getBatchById(@PathVariable Long batchId) {
        return inventoryBatchService.getBatchById(batchId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public InventoryBatch createBatch(@RequestBody InventoryBatch inventoryBatch) {
        return inventoryBatchService.createBatch(inventoryBatch);
    }

    @PutMapping("/{batchId}")
    public ResponseEntity<InventoryBatch> updateBatch(@PathVariable Long batchId, @RequestBody InventoryBatch batchDetails) {
        return ResponseEntity.ok(inventoryBatchService.updateBatch(batchId, batchDetails));
    }

    @DeleteMapping("/{batchId}")
    public ResponseEntity<Void> deleteBatch(@PathVariable Long batchId) {
        inventoryBatchService.deleteBatch(batchId);
        return ResponseEntity.noContent().build();
    }
}
