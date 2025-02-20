package com.example.SmartInventorySystem.service;

import com.example.SmartInventorySystem.dto.ProductRequestDTO;
import com.example.SmartInventorySystem.model.*;
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
    private final SalesTransactionRepository salesTransactionRepository;
    private final SalesItemRepository salesItemRepository;

    public SaleService(ProductRepository productRepository,
                       ProductRecipeRepository productRecipeRepository,
                       ProductInUseRepository productInUseRepository,
                       BatchArrivalItemRepository batchArrivalItemRepository,
                       SalesTransactionRepository salesTransactionRepository,
                       SalesItemRepository salesItemRepository) {
        this.productRepository = productRepository;
        this.productRecipeRepository = productRecipeRepository;
        this.productInUseRepository = productInUseRepository;
        this.batchArrivalItemRepository = batchArrivalItemRepository;
        this.salesTransactionRepository = salesTransactionRepository;
        this.salesItemRepository = salesItemRepository;
    }

    @Transactional
    public String processSaleProduct(List<ProductRequestDTO> productRequestDTOS) {
        StringBuilder responseMessage = new StringBuilder();
        SalesTransaction salesTransaction = new SalesTransaction();
        BigDecimal totalAmount = BigDecimal.ZERO;

        try {
            salesTransaction = salesTransactionRepository.save(salesTransaction); // Save to get transaction ID

            for (ProductRequestDTO productDTO : productRequestDTOS) {
                try {
                    Optional<Product> existingProduct = productRepository.findByBarcode(productDTO.getBarcode());

                    if (existingProduct.isEmpty()) {
                        responseMessage.append("Product not found for barcode: ").append(productDTO.getBarcode()).append("\n");
                        continue;
                    }

                    Product product = existingProduct.get();

                    if (product.getIsComposite()) {
                        // Process composite product by checking ingredients
                        List<ProductRecipe> ingredients = productRecipeRepository.findByFinalProduct(product);
                        boolean canProcess = true;

                        for (ProductRecipe ingredientRecipe : ingredients) {
                            Product ingredient = ingredientRecipe.getIngredient();
                            Optional<ProductInUse> ingredientStockOpt = productInUseRepository.findByProduct(ingredient);

                            if (ingredientStockOpt.isPresent()) {
                                ProductInUse ingredientStock = ingredientStockOpt.get();
                                BigDecimal requiredVolume = ingredientRecipe.getQuantityRequired().multiply(productDTO.getQuantity());

                                if (ingredientStock.getVolumeRemaining().compareTo(requiredVolume) >= 0) {
                                    ingredientStock.setVolumeRemaining(ingredientStock.getVolumeRemaining().subtract(requiredVolume));
                                    productInUseRepository.save(ingredientStock);
                                } else {
                                    responseMessage.append("Not enough stock for ingredient: ").append(ingredient.getProductName()).append("\n");
                                    canProcess = false;
                                    break;
                                }
                            } else {
                                responseMessage.append("Ingredient not found in stock: ").append(ingredient.getProductName()).append("\n");
                                canProcess = false;
                                break;
                            }
                        }

                        if (!canProcess) continue;
                    } else {
                        // Process standard product by checking batch stock
                        Optional<BatchArrivalItem> batchItemOpt = batchArrivalItemRepository.findByProductAndExpiryDate(product, productDTO.getExpirationDate());

                        if (batchItemOpt.isEmpty()) {
                            responseMessage.append("Batch not found for product: ").append(product.getProductName()).append("\n");
                            continue;
                        }

                        BatchArrivalItem batchItem = batchItemOpt.get();

                        if (batchItem.getQuantityRemaining().compareTo(productDTO.getQuantity()) >= 0) {
                            batchItem.setQuantityRemaining(batchItem.getQuantityRemaining().subtract(productDTO.getQuantity()));
                            batchArrivalItemRepository.save(batchItem);
                        } else {
                            responseMessage.append("Not enough stock for product: ").append(product.getProductName()).append("\n");
                            continue;
                        }
                    }

                    // Log Sale
                    SalesItem salesItem = new SalesItem();
                    salesItem.setSalesTransaction(salesTransaction);
                    salesItem.setProduct(product);
                    salesItem.setQuantity(productDTO.getQuantity());

                    salesItemRepository.save(salesItem);

                    // Calculate Total Amount
                    totalAmount = totalAmount.add(product.getPrice().multiply(productDTO.getQuantity()));

                    responseMessage.append("Processed sale for product: ").append(product.getProductName()).append("\n");
                } catch (Exception e) {
                    responseMessage.append("Error processing product ").append(productDTO.getBarcode()).append(": ").append(e.getMessage()).append("\n");
                }
            }

            // Update total amount in the transaction
            salesTransaction.setTotalAmount(totalAmount);
            salesTransactionRepository.save(salesTransaction);
        } catch (Exception e) {
            responseMessage.append("Error processing sale transaction: ").append(e.getMessage()).append("\n");
        }

        return responseMessage.toString();
    }
}
