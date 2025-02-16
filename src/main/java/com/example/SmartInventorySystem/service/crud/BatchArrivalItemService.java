package com.example.SmartInventorySystem.service.crud;

import com.example.SmartInventorySystem.model.BatchArrivalItem;
import com.example.SmartInventorySystem.repository.crud.BatchArrivalItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BatchArrivalItemService {

    private final BatchArrivalItemRepository batchArrivalItemRepository;

    public BatchArrivalItemService(BatchArrivalItemRepository batchArrivalItemRepository) {
        this.batchArrivalItemRepository = batchArrivalItemRepository;
    }

    public List<BatchArrivalItem> getAllBatchArrivalItems() {
        return batchArrivalItemRepository.findAll();
    }

    public Optional<BatchArrivalItem> getBatchArrivalItemById(Long id) {
        return batchArrivalItemRepository.findById(id);
    }

    public BatchArrivalItem createBatchArrivalItem(BatchArrivalItem batchArrivalItem) {
        return batchArrivalItemRepository.save(batchArrivalItem);
    }

    public BatchArrivalItem updateBatchArrivalItem(Long id, BatchArrivalItem updatedBatchArrivalItem) {
        return batchArrivalItemRepository.findById(id).map(item -> {
            item.setBatchArrival(updatedBatchArrivalItem.getBatchArrival());
            item.setProduct(updatedBatchArrivalItem.getProduct());
            item.setQuantityReceived(updatedBatchArrivalItem.getQuantityReceived());
            item.setQuantityRemaining(updatedBatchArrivalItem.getQuantityRemaining());
            item.setExpiryDate(updatedBatchArrivalItem.getExpiryDate());
            item.setUnitCost(updatedBatchArrivalItem.getUnitCost());
            item.setAddedBy(updatedBatchArrivalItem.getAddedBy());
            return batchArrivalItemRepository.save(item);
        }).orElse(null);
    }

    public void deleteBatchArrivalItem(Long id) {
        batchArrivalItemRepository.deleteById(id);
    }
}
