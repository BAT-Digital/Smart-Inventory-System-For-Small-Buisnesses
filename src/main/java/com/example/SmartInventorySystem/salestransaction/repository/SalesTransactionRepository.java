package com.example.SmartInventorySystem.salestransaction.repository;


import com.example.SmartInventorySystem.salestransaction.entity.SalesTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesTransactionRepository extends JpaRepository<SalesTransaction, Long> {
}
