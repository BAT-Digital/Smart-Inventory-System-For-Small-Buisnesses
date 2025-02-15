package com.example.SmartInventorySystem.controller;

import com.example.SmartInventorySystem.model.BatchArrivalItem;
import com.example.SmartInventorySystem.service.BatchArrivalItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/batch-arrival-items")
public class BatchArrivalItemController {

    private final BatchArrivalItemService batchArrivalItemService;

    public BatchArrivalItemController(BatchArrivalItemService batchArrivalItemService) {
        this.batchArrivalItemService = batchArrivalItemService;
    }

    @GetMapping
    public List<BatchArrivalItem> getAllBatchArrivalItems() {
        return batchArrivalItemService.getAllBatchArrivalItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchArrivalItem> getBatchArrivalItemById(@PathVariable Long id) {
        Optional<BatchArrivalItem> batchArrivalItem = batchArrivalItemService.getBatchArrivalItemById(id);
        return batchArrivalItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public BatchArrivalItem createBatchArrivalItem(@RequestBody BatchArrivalItem batchArrivalItem) {
        return batchArrivalItemService.createBatchArrivalItem(batchArrivalItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BatchArrivalItem> updateBatchArrivalItem(@PathVariable Long id, @RequestBody BatchArrivalItem batchArrivalItem) {
        BatchArrivalItem updatedBatchArrivalItem = batchArrivalItemService.updateBatchArrivalItem(id, batchArrivalItem);
        return updatedBatchArrivalItem != null ? ResponseEntity.ok(updatedBatchArrivalItem) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBatchArrivalItem(@PathVariable Long id) {
        batchArrivalItemService.deleteBatchArrivalItem(id);
        return ResponseEntity.noContent().build();
    }
}
