package com.example.SmartInventorySystem.repository;


import com.example.SmartInventorySystem.model.TransactionLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLineRepository extends JpaRepository<TransactionLine, Long> {
    // Custom queries can be added here if needed
}
