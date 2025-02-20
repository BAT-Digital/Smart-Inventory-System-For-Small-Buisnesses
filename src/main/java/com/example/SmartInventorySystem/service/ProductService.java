package com.example.SmartInventorySystem.service;

import com.example.SmartInventorySystem.dto.IncomingBatchProductRequestDTO;
import com.example.SmartInventorySystem.model.Product;

import com.example.SmartInventorySystem.repository.crud.CategoryRepository;
import com.example.SmartInventorySystem.repository.crud.ProductRepository;
import com.example.SmartInventorySystem.repository.crud.SupplierRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public String createProduct(IncomingBatchProductRequestDTO productRequestDTO) {
        // Check if product already exists using the barcode
        Optional<Product> existingProduct = productRepository.findByBarcode(productRequestDTO.getBarcode());
        if (existingProduct.isPresent()) {
            return "Product already exists with barcode: " + productRequestDTO.getBarcode();
        }

        // Create new product and map fields from DTO
        Product product = new Product();
        product.setProductName(productRequestDTO.getProductName());
        product.setBarcode(productRequestDTO.getBarcode());
        product.setIsPerishable(productRequestDTO.getIsPerishable());
        // Set composite to false (or map from DTO if available)
        product.setIsComposite(false);
        product.setUnitOfMeasure(productRequestDTO.getUnitOfMeasure());
        product.setDescription(productRequestDTO.getDescription());
        product.setPrice(productRequestDTO.getPrice());
        product.setVolume(productRequestDTO.getVolume());

        // Fetch and set Category (if exists)
        categoryRepository.findById(productRequestDTO.getCategoryId())
                .ifPresent(product::setCategory);

        // Fetch and set Supplier (if exists)
        supplierRepository.findById(productRequestDTO.getSupplierId())
                .ifPresent(product::setSupplier);
        // Save the new product
        productRepository.save(product);
        return "Created new product: " + productRequestDTO.getProductName();
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }


    public Product updateProduct(Long productId, Product productDetails) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));

        product.setProductName(productDetails.getProductName());
        product.setDescription(productDetails.getDescription());
        product.setIsPerishable(productDetails.getIsPerishable());

        return productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));

        productRepository.delete(product);
    }

    public Optional<Product> getProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode);
    }
}