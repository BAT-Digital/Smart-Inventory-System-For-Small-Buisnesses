package com.example.SmartInventorySystem.controller.crud;

import com.example.SmartInventorySystem.model.ProductRecipe;
import com.example.SmartInventorySystem.service.crud.ProductRecipeService;
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
