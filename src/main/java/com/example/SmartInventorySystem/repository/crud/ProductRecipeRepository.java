package com.example.SmartInventorySystem.repository.crud;

import com.example.SmartInventorySystem.model.Product;
import com.example.SmartInventorySystem.model.ProductRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRecipeRepository extends JpaRepository<ProductRecipe, Long> {
    List<ProductRecipe> findByFinalProduct(Product finalProduct);
    List<ProductRecipe> findByFinalProduct_ProductId(Long finalProductId);
}
