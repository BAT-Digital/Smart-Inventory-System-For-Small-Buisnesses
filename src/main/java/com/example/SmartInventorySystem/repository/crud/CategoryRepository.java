package com.example.SmartInventorySystem.repository.crud;

import com.example.SmartInventorySystem.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
