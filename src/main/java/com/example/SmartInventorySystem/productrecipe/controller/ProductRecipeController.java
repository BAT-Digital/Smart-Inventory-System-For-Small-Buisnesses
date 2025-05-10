package com.example.SmartInventorySystem.productrecipe.controller;


import com.example.SmartInventorySystem.productrecipe.entity.ProductRecipe;

import com.example.SmartInventorySystem.productrecipe.service.ProductRecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-recipes")
public class ProductRecipeController {

    private final ProductRecipeService productRecipeService;

    public ProductRecipeController(ProductRecipeService productRecipeService) {
        this.productRecipeService = productRecipeService;
    }

    @GetMapping
    public List<ProductRecipe> getAllProductRecipes() {
        return productRecipeService.getAllProductRecipes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductRecipe> getProductRecipeById(@PathVariable Long id) {
        Optional<ProductRecipe> productRecipe = productRecipeService.getProductRecipeById(id);
        return productRecipe.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/by-final-product-id/{productId}")
    public List<ProductRecipe> getByFinalProductId(@PathVariable Long productId) {
        return productRecipeService.getByFinalProductId(productId);
    }

    @PostMapping
    public ProductRecipe createProductRecipe(@RequestBody ProductRecipe productRecipe) {
        return productRecipeService.createProductRecipe(productRecipe);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductRecipe> updateProductRecipe(@PathVariable Long id, @RequestBody ProductRecipe productRecipe) {
        ProductRecipe updatedRecipe = productRecipeService.updateProductRecipe(id, productRecipe);
        return updatedRecipe != null ? ResponseEntity.ok(updatedRecipe) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductRecipe(@PathVariable Long id) {
        productRecipeService.deleteProductRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
