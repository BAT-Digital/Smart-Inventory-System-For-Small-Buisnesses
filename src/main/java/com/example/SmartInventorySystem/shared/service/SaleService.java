package com.example.SmartInventorySystem.shared.service;

import com.example.SmartInventorySystem.batcharrivalitem.entity.BatchArrivalItem;
import com.example.SmartInventorySystem.batcharrivalitem.repository.BatchArrivalItemRepository;
import com.example.SmartInventorySystem.product.entity.Product;
import com.example.SmartInventorySystem.product.repository.ProductRepository;
import com.example.SmartInventorySystem.productinuse.entity.ProductInUse;
import com.example.SmartInventorySystem.productinuse.repository.ProductInUseRepository;
import com.example.SmartInventorySystem.productrecipe.entity.ProductRecipe;
import com.example.SmartInventorySystem.productrecipe.repository.ProductRecipeRepository;
import com.example.SmartInventorySystem.saleitem.entity.SalesItem;
import com.example.SmartInventorySystem.saleitem.repository.SalesItemRepository;
import com.example.SmartInventorySystem.salestransaction.entity.SalesTransaction;
import com.example.SmartInventorySystem.salestransaction.repository.SalesTransactionRepository;
import com.example.SmartInventorySystem.shared.dto.ProductRequestDTO;
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
    private final  NotificationService notificationService;

    public SaleService(ProductRepository productRepository, ProductRecipeRepository productRecipeRepository, ProductInUseRepository productInUseRepository, BatchArrivalItemRepository batchArrivalItemRepository, SalesTransactionRepository salesTransactionRepository, SalesItemRepository salesItemRepository, NotificationService notificationService) {
        this.productRepository = productRepository;
        this.productRecipeRepository = productRecipeRepository;
        this.productInUseRepository = productInUseRepository;
        this.batchArrivalItemRepository = batchArrivalItemRepository;
        this.salesTransactionRepository = salesTransactionRepository;
        this.salesItemRepository = salesItemRepository;
        this.notificationService = notificationService;
    }

    public SalesTransaction createNewCheck(String credentials) {
        SalesTransaction salesTransaction = new SalesTransaction();
        salesTransaction.setCredentials(credentials);
        salesTransaction.setStatus("PROCESSING");
        salesTransaction.setTotalAmount(null); // Explicitly set to null
        return salesTransactionRepository.save(salesTransaction);
    }


    @Transactional
    public String processSellTransaction(Long transactionId, List<ProductRequestDTO> productRequestDTOS) {
        StringBuilder responseMessage = new StringBuilder();
        SalesTransaction salesTransaction = salesTransactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("SalesTransaction not found with ID: " + transactionId));
        BigDecimal totalAmount = BigDecimal.ZERO;

        try {
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
                        if (batchItem.getQuantityRemaining().compareTo(batchItem.getProduct().getThreshold()) < 0) notificationService.sendLowStockNotification(product.getProductName(), batchItem.getQuantityRemaining().intValue(), batchItem.getProduct().getThreshold());
                    }

                    // Calculate Total Amount
                    totalAmount = totalAmount.add(product.getPrice().multiply(productDTO.getQuantity()));

                    responseMessage.append("Processed sale for product: ").append(product.getProductName()).append("\n");

                } catch (Exception e) {
                    responseMessage.append("Error processing product ").append(productDTO.getBarcode()).append(": ").append(e.getMessage()).append("\n");
                }
            }

            // Update the sales transaction
            salesTransaction.setTotalAmount(totalAmount);
            salesTransaction.setStatus("COMPLETED");
            salesTransactionRepository.save(salesTransaction);
        } catch (Exception e) {
            responseMessage.append("Error processing sale transaction: ").append(e.getMessage()).append("\n");
        }

        return responseMessage.toString();
    }

    @Transactional
    public String cancelCheck(Long transactionId) {
        SalesTransaction salesTransaction = salesTransactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("SalesTransaction not found with ID: " + transactionId));

        // Delete associated sales items
        List<SalesItem> salesItems = salesItemRepository.findBySalesTransaction(salesTransaction);
        salesItemRepository.deleteAll(salesItems);

        // Update the sales transaction
        salesTransaction.setTotalAmount(BigDecimal.ZERO);
        salesTransaction.setStatus("CANCELED");
        salesTransactionRepository.save(salesTransaction);

        return "Sale transaction canceled successfully.";
    }
}
