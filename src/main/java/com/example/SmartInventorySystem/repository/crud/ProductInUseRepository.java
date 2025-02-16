package com.example.SmartInventorySystem.repository.crud;

import com.example.SmartInventorySystem.model.Product;
import com.example.SmartInventorySystem.model.ProductInUse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductInUseRepository extends JpaRepository<ProductInUse, Long> {
    Optional<ProductInUse> findByProduct(Product product);
}
