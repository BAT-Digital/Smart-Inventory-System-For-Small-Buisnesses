package com.example.SmartInventorySystem.supplier.repository;


import com.example.SmartInventorySystem.supplier.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
