package com.example.SmartInventorySystem.service;

import com.example.SmartInventorySystem.model.InventoryBatch;
import com.example.SmartInventorySystem.repository.InventoryBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryBatchService {

    @Autowired
    private InventoryBatchRepository inventoryBatchRepository;

    public List<InventoryBatch> getAllBatches() {
        return inventoryBatchRepository.findAll();
    }

    public Optional<InventoryBatch> getBatchById(Long batchId) {
        return inventoryBatchRepository.findById(batchId);
    }

    public InventoryBatch createBatch(InventoryBatch inventoryBatch) {
        return inventoryBatchRepository.save(inventoryBatch);
    }

    public InventoryBatch updateBatch(Long batchId, InventoryBatch batchDetails) {
        InventoryBatch batch = inventoryBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found with id " + batchId));

        batch.setProduct(batchDetails.getProduct());
        batch.setQuantity(batchDetails.getQuantity());
        batch.setUnitCost(batchDetails.getUnitCost());
        batch.setExpirationDate(batchDetails.getExpirationDate());
        batch.setStatus(batchDetails.getStatus());

        return inventoryBatchRepository.save(batch);
    }

    public void deleteBatch(Long batchId) {
        InventoryBatch batch = inventoryBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found with id " + batchId));

        inventoryBatchRepository.delete(batch);
    }
}
