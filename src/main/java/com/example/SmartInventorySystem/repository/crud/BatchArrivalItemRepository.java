package com.example.SmartInventorySystem.repository.crud;

import com.example.SmartInventorySystem.model.BatchArrivalItem;
import com.example.SmartInventorySystem.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BatchArrivalItemRepository extends JpaRepository<BatchArrivalItem, Long> {
    Optional<BatchArrivalItem> findByProductAndExpiryDate(Product product, LocalDate expiryDate);

    @Query("SELECT AVG(bai.quantityRemaining * bai.unitCost) " +
            "FROM BatchArrivalItem bai " +
            "WHERE bai.batchArrival.arrivalDate BETWEEN :startDate AND :endDate")
    BigDecimal findAverageInventoryValue(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


    @Query("SELECT DISTINCT bai.product " +
            "FROM BatchArrivalItem bai " +
            "WHERE bai.quantityRemaining = 0")
    List<Product> findProductsWithZeroRemainingStock();

    @Query("SELECT bai.product " +
            "FROM BatchArrivalItem bai " +
            "WHERE bai.quantityRemaining > 100 " + // Adjust threshold as needed
            "AND bai.product.productId NOT IN (" +
            "   SELECT si.product.productId FROM SalesItem si" +
            ")")
    List<Product> findExcessStockProducts();

    List<BatchArrivalItem> findByExpiryDateBefore(LocalDate thresholdDate);

    @Query("SELECT SUM(bai.quantityRemaining) " +
            "FROM BatchArrivalItem bai " +
            "WHERE bai.product.productId = :productId")
    BigDecimal findRemainingStockByProduct(@Param("productId") Long productId);
}
