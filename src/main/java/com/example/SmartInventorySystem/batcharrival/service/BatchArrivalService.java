package com.example.SmartInventorySystem.batcharrival.service;

import com.example.SmartInventorySystem.batcharrival.dto.BatchArrivalDTO;
import com.example.SmartInventorySystem.batcharrival.repository.BatchArrivalRepository;
import com.example.SmartInventorySystem.batcharrival.entity.BatchArrival;

import com.example.SmartInventorySystem.supplier.entity.Supplier;
import com.example.SmartInventorySystem.supplier.repository.SupplierRepository;
import com.example.SmartInventorySystem.user.entity.User;
import com.example.SmartInventorySystem.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<BatchArrival> getAllBatchArrivalsByIdDesc() {
        return batchArrivalRepository.findAllByOrderByArrivalIdDesc();
    }

    public List<BatchArrival> searchBatchArrivals(String searchTerm) {
        return batchArrivalRepository.search(
                searchTerm.toLowerCase() + "%");
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
