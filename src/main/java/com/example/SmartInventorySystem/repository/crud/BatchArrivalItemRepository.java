package com.example.SmartInventorySystem.repository.crud;

import com.example.SmartInventorySystem.model.BatchArrivalItem;
import com.example.SmartInventorySystem.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface BatchArrivalItemRepository extends JpaRepository<BatchArrivalItem, Long> {
    Optional<BatchArrivalItem> findByProductAndExpiryDate(Product product, LocalDate expiryDate);
}
