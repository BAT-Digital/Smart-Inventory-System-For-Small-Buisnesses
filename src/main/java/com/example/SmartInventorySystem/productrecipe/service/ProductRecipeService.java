package com.example.SmartInventorySystem.productrecipe.service;


import com.example.SmartInventorySystem.productrecipe.entity.ProductRecipe;
import com.example.SmartInventorySystem.productrecipe.repository.ProductRecipeRepository;
import com.example.SmartInventorySystem.product.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductRecipeService {

    private final ProductRecipeRepository productRecipeRepository;

    public ProductRecipeService(ProductRecipeRepository productRecipeRepository) {
        this.productRecipeRepository = productRecipeRepository;
    }

    public List<ProductRecipe> getAllProductRecipes() {
        return productRecipeRepository.findAll();
    }

    public List<ProductRecipe> getByFinalProduct(Product product) {
        return productRecipeRepository.findByFinalProduct(product);
    }

    public List<ProductRecipe> getByFinalProductId(Long productId) {
        return productRecipeRepository.findByFinalProduct_ProductId(productId);
    }

    public Optional<ProductRecipe> getProductRecipeById(Long id) {
        return productRecipeRepository.findById(id);
    }

    public ProductRecipe createProductRecipe(ProductRecipe productRecipe) {
        return productRecipeRepository.save(productRecipe);
    }

    public ProductRecipe updateProductRecipe(Long id, ProductRecipe updatedProductRecipe) {
        return productRecipeRepository.findById(id).map(recipe -> {
            recipe.setFinalProduct(updatedProductRecipe.getFinalProduct());
            recipe.setIngredient(updatedProductRecipe.getIngredient());
            recipe.setQuantityRequired(updatedProductRecipe.getQuantityRequired());
            return productRecipeRepository.save(recipe);
        }).orElse(null);
    }

    public void deleteProductRecipe(Long id) {
        productRecipeRepository.deleteById(id);
    }
}
