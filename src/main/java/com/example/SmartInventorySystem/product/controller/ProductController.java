package com.example.SmartInventorySystem.product.controller;

import com.example.SmartInventorySystem.shared.dto.IncomingBatchProductRequestDTO;
import com.example.SmartInventorySystem.product.entity.Product;
import com.example.SmartInventorySystem.product.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/search")
    public List<Product> getAllProductsSearch(
            @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return productService.searchAllProducts(search);
        }
        return productService.getAllProducts();
    }


    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        return productService.getProductById(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-category/{categoryName}")
    public List<Product> getByCategoryName(@PathVariable String categoryName, @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return productService.searchProductsByCategoryName(categoryName, search);
        }
        return productService.getProductsByCategoryName(categoryName);
    }

    @GetMapping("/by-supplier/{supplierName}")
    public List<Product> getBySupplierName(@PathVariable String supplierName, @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return productService.searchProductsBySupplierName(supplierName, search);
        }
        return productService.getProductsBySupplierName(supplierName);
    }

    @PostMapping
    public ResponseEntity<String> processProducts(@RequestBody IncomingBatchProductRequestDTO productRequestDTOS) {
        String result = productService.createProduct(productRequestDTOS);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product productDetails) {
        return ResponseEntity.ok(productService.updateProduct(productId, productDetails));
    }

    @GetMapping("/composite/{isComposite}")
    public List<Product> getProductsByIsComposite(@PathVariable Boolean isComposite, @RequestParam(required = false) String search) {
            if (search != null && !search.isEmpty()) {
                return productService.searchProductsByIsComposite(isComposite, search);
            }
        return productService.getProductsByIsComposite(isComposite);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<Product> getProductByBarcode(@PathVariable String barcode) {
        Optional<Product> product = productService.getProductByBarcode(barcode);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}