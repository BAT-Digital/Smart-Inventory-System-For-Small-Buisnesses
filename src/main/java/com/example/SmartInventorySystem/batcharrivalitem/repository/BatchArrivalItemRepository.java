package com.example.SmartInventorySystem.batcharrivalitem.repository;

import com.example.SmartInventorySystem.batcharrivalitem.entity.BatchArrivalItem;
import com.example.SmartInventorySystem.product.entity.Product;
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
    List<BatchArrivalItem> findByProductAndExpiryDateIsNull(Product product);
    List<BatchArrivalItem> findByProductAndExpiryDateIsNullOrderByBatchItemIdAsc(Product product);

    List<BatchArrivalItem> findByProduct_BarcodeAndQuantityRemainingGreaterThan(String barcode, BigDecimal quantity);
    List<BatchArrivalItem> findByProduct_BarcodeAndExpiryDate(String barcode, LocalDate expiryDate);

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

    @Query(value = """
        SELECT bai.* 
        FROM batch_arrival_items bai
        JOIN batch_arrivals ba ON bai.arrival_id = ba.arrival_id
        WHERE ba.arrival_date = (
            SELECT MIN(ba_inner.arrival_date)
            FROM batch_arrival_items bai_inner
            JOIN batch_arrivals ba_inner ON bai_inner.arrival_id = ba_inner.arrival_id
            WHERE bai_inner.product_id = bai.product_id
        )
        AND bai.quantity_remaining < 0.3 * bai.quantity_received
        """,
            nativeQuery = true)
    List<BatchArrivalItem> findOldestBatchesWithLowRemaining();

    List<BatchArrivalItem> findByBatchArrivalArrivalId(Long arrivalId);
}
