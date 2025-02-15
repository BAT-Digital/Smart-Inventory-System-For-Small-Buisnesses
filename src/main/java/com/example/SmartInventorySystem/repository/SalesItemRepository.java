package com.example.SmartInventorySystem.repository;

import com.example.SmartInventorySystem.model.SalesItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesItemRepository extends JpaRepository<SalesItem, Long> {
}
