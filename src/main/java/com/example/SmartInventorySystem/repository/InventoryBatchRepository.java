package com.example.SmartInventorySystem.repository;


import com.example.SmartInventorySystem.model.InventoryBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryBatchRepository extends JpaRepository<InventoryBatch, Long> {
}
