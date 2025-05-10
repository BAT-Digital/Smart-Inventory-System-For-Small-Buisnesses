package com.example.SmartInventorySystem.productinuse.repository;

import com.example.SmartInventorySystem.product.entity.Product;
import com.example.SmartInventorySystem.productinuse.entity.ProductInUse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductInUseRepository extends JpaRepository<ProductInUse, Long> {
    Optional<ProductInUse> findByProduct(Product product);
}
