package com.example.SmartInventorySystem.batcharrival.repository;

import com.example.SmartInventorySystem.batcharrival.entity.BatchArrival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BatchArrivalRepository extends JpaRepository<BatchArrival, Long> {
    @Query("SELECT b FROM BatchArrival b WHERE " +
            "LOWER(b.supplier.name) LIKE LOWER(concat('%', :searchTerm, '%')) OR " +
            "LOWER(b.notes) LIKE LOWER(concat('%', :searchTerm, '%')) OR " +
            "LOWER(b.addedBy.username) LIKE LOWER(concat('%', :searchTerm, '%'))" +
            "ORDER BY b.id DESC")
    List<BatchArrival> search(@Param("searchTerm") String searchTerm);

    List<BatchArrival> findAllByOrderByArrivalIdDesc();
}
