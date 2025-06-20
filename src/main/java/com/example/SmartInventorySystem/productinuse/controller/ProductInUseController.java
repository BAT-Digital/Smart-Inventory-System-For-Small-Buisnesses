package com.example.SmartInventorySystem.productinuse.controller;

import com.example.SmartInventorySystem.productinuse.service.ProductInUseService;
import com.example.SmartInventorySystem.productinuse.entity.ProductInUse;
import com.example.SmartInventorySystem.shared.dto.ProductRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products-in-use")
public class ProductInUseController {

    private final ProductInUseService productInUseService;

    public ProductInUseController(ProductInUseService productInUseService) {
        this.productInUseService = productInUseService;
    }



    @PostMapping("/move-to-product-in-use")
    public ResponseEntity<String> moveToProductInUse(@RequestBody List<ProductRequestDTO> productRequestDTOS) {
        String result = productInUseService.moveToProductInUse(productRequestDTOS);
        return ResponseEntity.ok(result);
    }



    @GetMapping
    public List<ProductInUse> getAllProductsInUse() {
        return productInUseService.getAllProductsInUse();
    }

    @GetMapping("/search")
    public List<ProductInUse> getAllProductsInUseSearch(
            @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return productInUseService.searchProductsInUse(search);
        }
        return productInUseService.getAllProductsInUse();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductInUse> getProductInUseById(@PathVariable Long id) {
        Optional<ProductInUse> productInUse = productInUseService.getProductInUseById(id);
        return productInUse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ProductInUse createProductInUse(@RequestBody ProductInUse productInUse) {
        return productInUseService.createProductInUse(productInUse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductInUse> updateProductInUse(@PathVariable Long id, @RequestBody ProductInUse productInUse) {
        ProductInUse updatedProductInUse = productInUseService.updateProductInUse(id, productInUse);
        return updatedProductInUse != null ? ResponseEntity.ok(updatedProductInUse) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductInUse(@PathVariable Long id) {
        productInUseService.deleteProductInUse(id);
        return ResponseEntity.noContent().build();
    }
}
