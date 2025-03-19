package com.example.SmartInventorySystem.controller;

import com.example.SmartInventorySystem.dto.BatchArrivalItemDTO;
import com.example.SmartInventorySystem.model.BatchArrivalItem;
import com.example.SmartInventorySystem.service.BatchArrivalItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/{arrivalId}/items")
    public List<BatchArrivalItem> getBatchArrivalItems(@PathVariable Long arrivalId) {
        return batchArrivalItemService.getBatchArrivalItemsByArrivalId(arrivalId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchArrivalItem> getBatchArrivalItemById(@PathVariable Long id) {
        Optional<BatchArrivalItem> batchArrivalItem = batchArrivalItemService.getBatchArrivalItemById(id);
        return batchArrivalItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public ResponseEntity<String> processBatchArrivalItems(@RequestBody List<BatchArrivalItemDTO> batchArrivalItemDTOs) {
        String result = batchArrivalItemService.processBatchArrivalItems(batchArrivalItemDTOs);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BatchArrivalItem> updateBatchArrivalItem(@PathVariable Long id, @RequestBody BatchArrivalItem batchArrivalItem) {
        BatchArrivalItem updatedBatchArrivalItem = batchArrivalItemService.updateBatchArrivalItem(id, batchArrivalItem);
        return updatedBatchArrivalItem != null ? ResponseEntity.ok(updatedBatchArrivalItem) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBatchArrivalItem(@PathVariable Long id) {
        batchArrivalItemService.deleteBatchArrivalItem(id);
        return ResponseEntity.noContent().build();
    }
}
