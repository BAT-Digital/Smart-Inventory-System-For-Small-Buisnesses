package com.example.SmartInventorySystem.repository.crud;

import com.example.SmartInventorySystem.model.WriteOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface WriteOffRepository extends JpaRepository<WriteOff, Long> {
    @Query("SELECT SUM(wo.quantity * bai.unitCost) " +
            "FROM WriteOff wo " +
            "JOIN wo.batch bai")
    BigDecimal findTotalCostOfWaste();
}
