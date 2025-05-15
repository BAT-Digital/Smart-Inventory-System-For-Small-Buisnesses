package com.example.SmartInventorySystem.category.repository;

import com.example.SmartInventorySystem.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE " +
            "LOWER(c.name) LIKE LOWER(concat('%', :searchTerm, '%'))")
    List<Category> search(@Param("searchTerm") String searchTerm);
}
