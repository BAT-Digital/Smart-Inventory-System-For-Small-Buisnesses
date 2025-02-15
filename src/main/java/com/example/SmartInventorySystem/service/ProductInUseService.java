package com.example.SmartInventorySystem.service;

import com.example.SmartInventorySystem.model.ProductInUse;
import com.example.SmartInventorySystem.repository.ProductInUseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductInUseService {

    private final ProductInUseRepository productInUseRepository;

    public ProductInUseService(ProductInUseRepository productInUseRepository) {
        this.productInUseRepository = productInUseRepository;
    }

    public List<ProductInUse> getAllProductsInUse() {
        return productInUseRepository.findAll();
    }

    public Optional<ProductInUse> getProductInUseById(Long id) {
        return productInUseRepository.findById(id);
    }

    public ProductInUse createProductInUse(ProductInUse productInUse) {
        return productInUseRepository.save(productInUse);
    }

    public ProductInUse updateProductInUse(Long id, ProductInUse updatedProductInUse) {
        return productInUseRepository.findById(id).map(productInUse -> {
            productInUse.setDeassignedDate(updatedProductInUse.getDeassignedDate());
            productInUse.setVolume(updatedProductInUse.getVolume());
            return productInUseRepository.save(productInUse);
        }).orElse(null);
    }

    public void deleteProductInUse(Long id) {
        productInUseRepository.deleteById(id);
    }
}
