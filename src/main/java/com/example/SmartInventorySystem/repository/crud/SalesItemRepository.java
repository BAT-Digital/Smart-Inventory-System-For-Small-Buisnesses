package com.example.SmartInventorySystem.repository.crud;

import com.example.SmartInventorySystem.model.SalesItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SalesItemRepository extends JpaRepository<SalesItem, Long> {
    @Query("SELECT si.product.productId AS productId, " +
            "SUM(si.quantity) AS unitsSold, " +
            "SUM(si.quantity * si.product.price) AS revenue " +
            "FROM SalesItem si " +
            "GROUP BY si.product.productId")
    List<Object[]> findProductPerformance();

    // Aggregate sold quantities by day and product
    @Query("SELECT FUNCTION('DATE', st.transactionDate) AS date, " +
            "si.product.productId AS productId, " +
            "SUM(si.quantity) AS unitsSold " +
            "FROM SalesItem si JOIN si.salesTransaction st " +
            "GROUP BY FUNCTION('DATE', st.transactionDate), si.product.productId")
    List<Object[]> findDailyProductSales();
}
