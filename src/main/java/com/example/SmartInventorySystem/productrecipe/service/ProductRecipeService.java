package com.example.SmartInventorySystem.productrecipe.service;


import com.example.SmartInventorySystem.product.repository.ProductRepository;
import com.example.SmartInventorySystem.productrecipe.dto.ProductRecipeDTO;
import com.example.SmartInventorySystem.productrecipe.entity.ProductRecipe;
import com.example.SmartInventorySystem.productrecipe.repository.ProductRecipeRepository;
import com.example.SmartInventorySystem.product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductRecipeService {
    @Autowired
    private ProductRepository productRepository;

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

    public String processProductRecipes(List<ProductRecipeDTO> productRecipeDTOS) {
        for (ProductRecipeDTO productRecipeDTO : productRecipeDTOS) {
            try {
                ProductRecipe productRecipe = new ProductRecipe();
                productRecipe.setQuantityRequired(productRecipeDTO.getQuantityRequired());

                productRepository.findById(productRecipeDTO.getFinalProductId())
                        .ifPresent(productRecipe::setFinalProduct);

                productRepository.findById(productRecipeDTO.getIngredientId())
                        .ifPresent(productRecipe::setIngredient);

                productRecipeRepository.save(productRecipe);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return "Success";
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
