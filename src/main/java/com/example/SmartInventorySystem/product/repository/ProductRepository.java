package com.example.SmartInventorySystem.product.repository;

import com.example.SmartInventorySystem.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByBarcode(String barcode);
    List<Product> findByIsComposite(Boolean isComposite);
    List<Product> findByCategory_Name(String categoryName);
    List<Product> findBySupplier_Name(String supplierName);
}
