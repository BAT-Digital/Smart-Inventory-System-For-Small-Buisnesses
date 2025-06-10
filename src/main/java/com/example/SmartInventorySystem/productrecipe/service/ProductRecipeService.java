package com.example.SmartInventorySystem.productrecipe.service;


import com.example.SmartInventorySystem.product.repository.ProductRepository;
import com.example.SmartInventorySystem.productrecipe.dto.ProductRecipeDTO;
import com.example.SmartInventorySystem.productrecipe.entity.ProductRecipe;
import com.example.SmartInventorySystem.productrecipe.repository.ProductRecipeRepository;
import com.example.SmartInventorySystem.product.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductRecipeService {
    private final ProductRepository productRepository;

    private final ProductRecipeRepository productRecipeRepository;

    public ProductRecipeService(ProductRecipeRepository productRecipeRepository, ProductRepository productRepository) {
        this.productRecipeRepository = productRecipeRepository;
        this.productRepository = productRepository;
    }

    public List<ProductRecipe> getAllProductRecipes() {
        return productRecipeRepository.findAll();
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

    @Transactional
    public ResponseEntity<?> processProductRecipes(List<ProductRecipeDTO> productRecipeDTOS) {
        List<ProductRecipe> savedRecipes = new ArrayList<>();

        for (ProductRecipeDTO dto : productRecipeDTOS) {

            if (dto.getFinalProductId() == null || dto.getIngredientId() == null) {
                return ResponseEntity.badRequest().body("Missing product or ingredient ID in input");
            }

            ProductRecipe recipe = new ProductRecipe();
            recipe.setQuantityRequired(dto.getQuantityRequired());

            Optional<Product> finalProduct = productRepository.findById(dto.getFinalProductId());
            Optional<Product> ingredient = productRepository.findById(dto.getIngredientId());

            if (finalProduct.isEmpty() || ingredient.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found for ID: " +
                        dto.getFinalProductId() + " or Ingredient ID: " + dto.getIngredientId());
            }

            recipe.setFinalProduct(finalProduct.get());
            recipe.setIngredient(ingredient.get());

            savedRecipes.add(productRecipeRepository.save(recipe));
        }

        return ResponseEntity.ok("Recipes processed: " + savedRecipes.size());
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
