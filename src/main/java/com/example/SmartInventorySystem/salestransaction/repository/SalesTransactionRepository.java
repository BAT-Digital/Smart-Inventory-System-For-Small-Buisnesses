package com.example.SmartInventorySystem.salestransaction.repository;


import com.example.SmartInventorySystem.salestransaction.entity.SalesTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesTransactionRepository extends JpaRepository<SalesTransaction, Long> {
    List<SalesTransaction> findByStatus(String status);
}
