package com.example.SmartInventorySystem.service;

import com.example.SmartInventorySystem.model.BatchArrival;
import com.example.SmartInventorySystem.model.Supplier;
import com.example.SmartInventorySystem.model.User;
import com.example.SmartInventorySystem.repository.crud.BatchArrivalRepository;
import com.example.SmartInventorySystem.repository.crud.SupplierRepository;
import com.example.SmartInventorySystem.repository.crud.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.SmartInventorySystem.dto.BatchArrivalDTO;

import java.util.List;
import java.util.Optional;

@Service
public class BatchArrivalService {

    private final BatchArrivalRepository batchArrivalRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private UserRepository userRepository;
    public BatchArrivalService(BatchArrivalRepository batchArrivalRepository) {
        this.batchArrivalRepository = batchArrivalRepository;
    }

    public List<BatchArrival> getAllBatchArrivals() {
        return batchArrivalRepository.findAll();
    }

    public Optional<BatchArrival> getBatchArrivalById(Long id) {
        return batchArrivalRepository.findById(id);
    }

    @Transactional
    public BatchArrival processBatchArrival(BatchArrivalDTO dto) {
        // Fetch Supplier
        Optional<Supplier> supplierOpt = supplierRepository.findById(dto.getSupplierId());
        if (!supplierOpt.isPresent()) {
            return null;
        }
        Supplier supplier = supplierOpt.get();

        // Create and map BatchArrival fields
        BatchArrival batchArrival = new BatchArrival();
        batchArrival.setSupplier(supplier);
        batchArrival.setNotes(dto.getNotes());

        // Fetch and set the User who added this batch arrival.
        Optional<User> userOpt = userRepository.findById(dto.getAddedById());
        if (!userOpt.isPresent()) {
            return null;
        }
        batchArrival.setAddedBy(userOpt.get());

        // Save the new BatchArrival
        batchArrivalRepository.save(batchArrival);
        return batchArrival;
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
