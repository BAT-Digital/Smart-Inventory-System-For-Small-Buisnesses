package com.example.SmartInventorySystem.repository.crud;

import com.example.SmartInventorySystem.model.BatchArrivalItem;
import com.example.SmartInventorySystem.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BatchArrivalItemRepository extends JpaRepository<BatchArrivalItem, Long> {
    Optional<BatchArrivalItem> findByProductAndExpiryDate(Product product, LocalDate expiryDate);

    @Query("SELECT bai.product.productId, " +
            "SUM(bai.unitCost * bai.quantityReceived) / SUM(bai.quantityReceived) " +
            "FROM BatchArrivalItem bai " +
            "GROUP BY bai.product.productId")
    List<Object[]> findAverageUnitCostPerProduct();
}
