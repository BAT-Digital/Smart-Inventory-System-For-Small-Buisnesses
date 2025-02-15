package com.example.SmartInventorySystem.repository;

import com.example.SmartInventorySystem.model.ProductInUse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInUseRepository extends JpaRepository<ProductInUse, Long> {
}
