package com.example.SmartInventorySystem.repository;

import com.example.SmartInventorySystem.model.ProductRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRecipeRepository extends JpaRepository<ProductRecipe, Long> {
}
