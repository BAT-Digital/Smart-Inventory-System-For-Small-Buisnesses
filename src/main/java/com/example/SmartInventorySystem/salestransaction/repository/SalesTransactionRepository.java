package com.example.SmartInventorySystem.salestransaction.repository;


import com.example.SmartInventorySystem.salestransaction.entity.SalesTransaction;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;
import java.util.List;

public interface SalesTransactionRepository extends JpaRepository<SalesTransaction, Long> {
    List<SalesTransaction> findByStatus(String status);

    List<SalesTransaction> findByTransactionDateBetweenAndStatusOrderByTransactionDateDesc(
            LocalDateTime start,
            LocalDateTime end,
            String status
    );
}
