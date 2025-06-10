package com.example.SmartInventorySystem.writeoff.repository;


import com.example.SmartInventorySystem.writeoff.entity.WriteOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WriteOffRepository extends JpaRepository<WriteOff, Long> {

    @Query("SELECT w FROM WriteOff w WHERE " +
            "LOWER(w.batch.product.productName) LIKE LOWER(concat('%', :searchTerm, '%')) OR " +
            "LOWER(w.reason) LIKE LOWER(concat('%', :searchTerm, '%'))")
    List<WriteOff> search(@Param("searchTerm") String searchTerm);
}
