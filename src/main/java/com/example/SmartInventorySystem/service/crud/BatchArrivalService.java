package com.example.SmartInventorySystem.service.crud;

import com.example.SmartInventorySystem.model.BatchArrival;
import com.example.SmartInventorySystem.repository.crud.BatchArrivalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BatchArrivalService {

    private final BatchArrivalRepository batchArrivalRepository;

    public BatchArrivalService(BatchArrivalRepository batchArrivalRepository) {
        this.batchArrivalRepository = batchArrivalRepository;
    }

    public List<BatchArrival> getAllBatchArrivals() {
        return batchArrivalRepository.findAll();
    }

    public Optional<BatchArrival> getBatchArrivalById(Long id) {
        return batchArrivalRepository.findById(id);
    }

    public BatchArrival createBatchArrival(BatchArrival batchArrival) {
        return batchArrivalRepository.save(batchArrival);
    }

    public BatchArrival updateBatchArrival(Long id, BatchArrival updatedBatchArrival) {
        return batchArrivalRepository.findById(id).map(batch -> {
            batch.setSupplier(updatedBatchArrival.getSupplier());
            batch.setArrivalDate(updatedBatchArrival.getArrivalDate());
            batch.setNotes(updatedBatchArrival.getNotes());
            return batchArrivalRepository.save(batch);
        }).orElse(null);
    }

    public void deleteBatchArrival(Long id) {
        batchArrivalRepository.deleteById(id);
    }
}
