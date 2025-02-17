package com.example.SmartInventorySystem.service;

import com.example.SmartInventorySystem.dto.ProductRequestDTO;
import com.example.SmartInventorySystem.model.BatchArrivalItem;
import com.example.SmartInventorySystem.model.Product;
import com.example.SmartInventorySystem.model.ProductInUse;
import com.example.SmartInventorySystem.model.ProductRecipe;
import com.example.SmartInventorySystem.repository.crud.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {
    private final ProductRepository productRepository;
    private final ProductRecipeRepository productRecipeRepository;
    private final ProductInUseRepository productInUseRepository;
    private final BatchArrivalItemRepository batchArrivalItemRepository;


    public SaleService(ProductRepository productRepository,
                       ProductRecipeRepository productRecipeRepository,
                       ProductInUseRepository productInUseRepository,
                       BatchArrivalItemRepository batchArrivalItemRepository) {
        this.productRepository = productRepository;
        this.productRecipeRepository = productRecipeRepository;
        this.productInUseRepository = productInUseRepository;
        this.batchArrivalItemRepository = batchArrivalItemRepository;
    }

    @Transactional
    public String processSaleProduct(List<ProductRequestDTO> productRequestDTOS) {
        StringBuilder responseMessage = new StringBuilder();

        for (ProductRequestDTO productDTO : productRequestDTOS) {
            Optional<Product> existingProduct = productRepository.findByBarcode(productDTO.getBarcode());

            if (existingProduct.isPresent()) {
                Product product = existingProduct.get();

                if (product.getIsComposite()) {
                    List<ProductRecipe> ingredients = productRecipeRepository.findByFinalProduct(product);

                    for (ProductRecipe ingredientRecipe : ingredients) {
                        Product ingredient = ingredientRecipe.getIngredient();

                        // Find ingredient stock in ProductInUse
                        Optional<ProductInUse> ingredientStockOpt = productInUseRepository.findByProduct(ingredient);

                        if (ingredientStockOpt.isPresent()) {
                            ProductInUse ingredientStock = ingredientStockOpt.get();
                            BigDecimal requiredVolume = ingredientRecipe.getQuantityRequired()
                                    .multiply(productDTO.getQuantity());

                            if (ingredientStock.getVolumeRemaining().compareTo(requiredVolume) >= 0) {
                                // Subtract required volume from stock
                                ingredientStock.setVolumeRemaining(ingredientStock.getVolumeRemaining().subtract(requiredVolume));
                                productInUseRepository.save(ingredientStock);
                                responseMessage.append("Updated ingredient: ").append(ingredient.getProductName()).append("\n");
                            } else {
                                responseMessage.append("Not enough stock for ingredient: ").append(ingredient.getProductName()).append("\n");
                            }
                        } else {
                            responseMessage.append("Ingredient not found in stock: ").append(ingredient.getProductName()).append("\n");
                        }
                    }
                } else {
                    Optional<BatchArrivalItem> batchItemOpt = batchArrivalItemRepository.findByProductAndExpiryDate(
                            product, productDTO.getExpirationDate()
                    );

                    if (batchItemOpt.isPresent()) {
                        BatchArrivalItem batchItem = batchItemOpt.get();

                        if (batchItem.getQuantityRemaining().compareTo(productDTO.getQuantity()) >= 0) {
                            batchItem.setQuantityRemaining(batchItem.getQuantityRemaining().subtract(productDTO.getQuantity()));
                            batchArrivalItemRepository.save(batchItem);
                            responseMessage.append("Updated stock for product: ").append(product.getProductName()).append("\n");
                        } else {
                            responseMessage.append("Not enough stock for product: ").append(product.getProductName()).append("\n");
                        }
                    } else {
                        responseMessage.append("Batch not found for product: ").append(product.getProductName()).append("\n");
                    }
                }
            }
        }

        return responseMessage.toString();
    }
}
