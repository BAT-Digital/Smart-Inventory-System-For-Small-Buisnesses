package com.example.SmartInventorySystem.productrecipe.repository;

import com.example.SmartInventorySystem.product.entity.Product;

import com.example.SmartInventorySystem.productrecipe.entity.ProductRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRecipeRepository extends JpaRepository<ProductRecipe, Long> {
    List<ProductRecipe> findByFinalProduct(Product finalProduct);
    List<ProductRecipe> findByFinalProduct_ProductId(Long finalProductId);
}
