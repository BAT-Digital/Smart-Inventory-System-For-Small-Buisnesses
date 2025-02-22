package com.example.SmartInventorySystem.repository.crud;

import com.example.SmartInventorySystem.model.SalesTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SalesTransactionRepository extends JpaRepository<SalesTransaction, Long> {
    // Aggregates daily sales: date, total revenue and count of transactions
    @Query("SELECT FUNCTION('DATE', st.transactionDate) AS date, " +
            "SUM(st.totalAmount) AS totalRevenue, " +
            "COUNT(st) AS transactionCount " +
            "FROM SalesTransaction st " +
            "GROUP BY FUNCTION('DATE', st.transactionDate)")
    List<Object[]> findDailySalesTrends();


    @Query("SELECT FUNCTION('DATE', st.transactionDate) AS date, " +
            "SUM(st.totalAmount) AS revenue " +
            "FROM SalesTransaction st " +
            "GROUP BY FUNCTION('DATE', st.transactionDate)")
    List<Object[]> findDailyRevenue();
}
