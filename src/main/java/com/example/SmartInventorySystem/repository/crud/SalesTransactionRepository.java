package com.example.SmartInventorySystem.repository.crud;

import com.example.SmartInventorySystem.model.SalesTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesTransactionRepository extends JpaRepository<SalesTransaction, Long> {
}
