package com.example.SmartInventorySystem.productinuse.repository;

import com.example.SmartInventorySystem.product.entity.Product;
import com.example.SmartInventorySystem.productinuse.entity.ProductInUse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductInUseRepository extends JpaRepository<ProductInUse, Long> {
    Optional<ProductInUse> findByProduct(Product product);

    @Query("SELECT p FROM ProductInUse p WHERE " +
            "LOWER(p.product.productName) LIKE LOWER(concat('%', :searchTerm, '%')) OR " +
            "LOWER(p.assignedBy.username) LIKE LOWER(concat('%', :searchTerm, '%'))")
    List<ProductInUse> search(@Param("searchTerm") String searchTerm);
}
