package com.example.SmartInventorySystem.product.repository;

import com.example.SmartInventorySystem.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByBarcode(String barcode);
    List<Product> findByIsComposite(Boolean isComposite);
    List<Product> findByCategory_Name(String categoryName);
    List<Product> findBySupplier_Name(String supplierName);

    @Query("SELECT p FROM Product p WHERE " +
            "LOWER(p.productName) LIKE LOWER(concat('%', :searchTerm, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(concat('%', :searchTerm, '%'))")
    List<Product> search(@Param("searchTerm") String searchTerm);

    @Query("SELECT p FROM Product p WHERE " +
            "p.isComposite = :isComposite AND " +
            "(LOWER(p.productName) LIKE LOWER(concat('%', :searchTerm, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(concat('%', :searchTerm, '%')))")
    List<Product> searchByComposite(
            @Param("isComposite") Boolean isComposite,
            @Param("searchTerm") String searchTerm);

    @Query("SELECT p FROM Product p WHERE " +
            "p.category.name = :categoryName AND " +
            "(LOWER(p.productName) LIKE LOWER(concat('%', :searchTerm, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(concat('%', :searchTerm, '%')))")
    List<Product> findByCategoryNameSearch(
            @Param("categoryName") String categoryName,
            @Param("searchTerm") String searchTerm);

    @Query("SELECT p FROM Product p WHERE " +
            "p.supplier.name = :supplierName AND " +
            "(LOWER(p.productName) LIKE LOWER(concat('%', :searchTerm, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(concat('%', :searchTerm, '%')))")
    List<Product> findBySupplierNameSearch(
            @Param("supplierName") String supplierName,
            @Param("searchTerm") String searchTerm);
}
