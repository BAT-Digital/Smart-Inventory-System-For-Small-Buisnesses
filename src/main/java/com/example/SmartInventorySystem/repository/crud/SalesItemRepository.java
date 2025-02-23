package com.example.SmartInventorySystem.repository.crud;

import com.example.SmartInventorySystem.model.SalesItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesItemRepository extends JpaRepository<SalesItem, Long> {
}
