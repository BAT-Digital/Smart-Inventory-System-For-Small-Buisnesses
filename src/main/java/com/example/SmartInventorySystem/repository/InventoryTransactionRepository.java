package com.example.SmartInventorySystem.repository;

import com.example.SmartInventorySystem.model.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    // Add custom queries if necessary (e.g., find transactions by type or date range)
}