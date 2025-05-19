package com.example.SmartInventorySystem.saleitem.repository;


import com.example.SmartInventorySystem.saleitem.entity.SalesItem;
import com.example.SmartInventorySystem.salestransaction.entity.SalesTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SalesItemRepository extends JpaRepository<SalesItem, Long> {
    @Query("SELECT SUM(si.quantity * bai.unitCost) " +
            "FROM SalesItem si " +
            "JOIN si.salesTransaction st " +
            "JOIN si.product p " +
            "JOIN BatchArrivalItem bai ON bai.product.productId = p.productId " +
            "WHERE st.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal findTotalCOGS(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


    @Query("SELECT DATE(st.transactionDate) AS saleDate, SUM(si.quantity * p.price) AS totalSales " +
            "FROM SalesItem si " +
            "JOIN si.salesTransaction st " +
            "JOIN si.product p " +
            "WHERE st.transactionDate BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(st.transactionDate)")
    Map<LocalDate, BigDecimal> findSalesTrends(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p.category, SUM(si.quantity * p.price) AS totalSales " +
            "FROM SalesItem si " +
            "JOIN si.product p " +
            "GROUP BY p.category")
    List<Object[]> findSalesByCategory();

    @Query("SELECT si.product, SUM(si.quantity * si.product.price) AS totalRevenue " +
            "FROM SalesItem si " +
            "GROUP BY si.product " +
            "ORDER BY totalRevenue DESC")
    List<Object[]> findProductsByRevenue();

    @Query("SELECT SUM(si.quantity * p.price) " +
            "FROM SalesItem si " +
            "JOIN si.product p " +
            "WHERE p.productId = :productId")
    BigDecimal findTotalSalesByProduct(@Param("productId") Long productId);

    List<SalesItem> findBySalesTransaction_TransactionId(Long transactionId);

    List<SalesItem> findBySalesTransaction(SalesTransaction salesTransaction);

    @Query("""
    SELECT FUNCTION('DATE', si.salesTransaction.transactionDate),
           si.product.productId,
           SUM(si.quantity)
    FROM SalesItem si
    GROUP BY FUNCTION('DATE', si.salesTransaction.transactionDate), si.product.productId
""")
    List<Object[]> fetchAggregatedSales();

    @Modifying
    @Query("DELETE FROM SalesItem si WHERE si.salesTransaction.transactionId = :transactionId")
    void deleteBySalesTransactionId(@Param("transactionId") Long transactionId);

    @Query("SELECT si FROM SalesItem si " +
            "WHERE si.salesTransaction.transactionId = :transactionId " +
            "AND si.product.productId = :productId " +
            "AND si.expiryDate = :expiryDate")
    Optional<SalesItem> findByTransactionAndProductAndExpiryDate(
            @Param("transactionId") Long transactionId,
            @Param("productId") Long productId,
            @Param("expiryDate") LocalDate expiryDate
    );

    @Query("SELECT si FROM SalesItem si " +
            "WHERE si.salesTransaction.transactionId = :transactionId " +
            "AND si.product.productId = :productId ")
    Optional<SalesItem> findByTransactionAndProduct(
            @Param("transactionId") Long transactionId,
            @Param("productId") Long productId
    );

}
