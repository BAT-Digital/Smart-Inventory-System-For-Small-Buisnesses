package com.example.SmartInventorySystem.controller.crud;

import com.example.SmartInventorySystem.dto.BatchArrivalDTO;
import com.example.SmartInventorySystem.model.BatchArrival;
import com.example.SmartInventorySystem.service.crud.BatchArrivalService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> processBatchArrivals(@RequestBody List<BatchArrivalDTO> batchArrivalDTOs) {
        String result = batchArrivalService.processBatchArrivals(batchArrivalDTOs);
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
