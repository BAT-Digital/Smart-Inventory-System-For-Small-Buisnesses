package com.example.SmartInventorySystem.batcharrival.controller;

import com.example.SmartInventorySystem.batcharrival.dto.BatchArrivalDTO;
import com.example.SmartInventorySystem.batcharrival.entity.BatchArrival;
import com.example.SmartInventorySystem.batcharrival.service.BatchArrivalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/batch-arrivals")
public class BatchArrivalController {

    private final BatchArrivalService batchArrivalService;

    public BatchArrivalController(BatchArrivalService batchArrivalService) {
        this.batchArrivalService = batchArrivalService;
    }

    @GetMapping
    public List<BatchArrival> getAllBatchArrivals() {
        return batchArrivalService.getAllBatchArrivals();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchArrival> getBatchArrivalById(@PathVariable Long id) {
        Optional<BatchArrival> batchArrival = batchArrivalService.getBatchArrivalById(id);
        return batchArrival.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BatchArrival> processBatchArrivals(@RequestBody BatchArrivalDTO batchArrivalDTOs) {
        BatchArrival result = batchArrivalService.processBatchArrival(batchArrivalDTOs);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BatchArrival> updateBatchArrival(@PathVariable Long id, @RequestBody BatchArrival batchArrival) {
        BatchArrival updatedBatchArrival = batchArrivalService.updateBatchArrival(id, batchArrival);
        return updatedBatchArrival != null ? ResponseEntity.ok(updatedBatchArrival) : ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBatchArrival(@PathVariable Long id) {
        batchArrivalService.deleteBatchArrival(id);
        return ResponseEntity.noContent().build();
    }
}
