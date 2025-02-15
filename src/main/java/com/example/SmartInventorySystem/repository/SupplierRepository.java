package com.example.SmartInventorySystem.repository;

import com.example.SmartInventorySystem.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
