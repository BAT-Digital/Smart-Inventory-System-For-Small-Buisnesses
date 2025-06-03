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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        SalesTransaction salesTransaction = salesTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("SalesTransaction not found with ID: " + transactionId));
        BigDecimal totalAmount = BigDecimal.ZERO;

        // First validate everything can be processed
        for (ProductRequestDTO productDTO : productRequestDTOS) {
            Optional<Product> existingProduct = productRepository.findByBarcode(productDTO.getBarcode());
            if (existingProduct.isEmpty()) {
                return ("Product not found for barcode: " + productDTO.getBarcode());
            }

            Product product = existingProduct.get();

            if (product.getIsComposite()) {
                List<ProductRecipe> ingredients = productRecipeRepository.findByFinalProduct(product);
                for (ProductRecipe ingredientRecipe : ingredients) {
                    Product ingredient = ingredientRecipe.getIngredient();
                    ProductInUse ingredientStock = productInUseRepository.findByProduct(ingredient)
                            .orElseThrow(() -> new RuntimeException("Ingredient not found in stock: " + ingredient.getProductName()));

                    BigDecimal requiredVolume = ingredientRecipe.getQuantityRequired().multiply(productDTO.getQuantity());
                    if (ingredientStock.getVolumeRemaining().compareTo(requiredVolume) < 0) {
                        return ("Not enough stock for ingredient: " + ingredient.getProductName());
                    }
                }
            } else {
                // Handle differently based on whether expiry date is specified
                if (productDTO.getExpirationDate() != null) {
                    // Original behavior - exact match by expiry date
                    BatchArrivalItem batchItem = batchArrivalItemRepository.findByProductAndExpiryDate(product, productDTO.getExpirationDate())
                            .orElseThrow(() -> new RuntimeException("Batch not found for product: " + product.getProductName()));

                    if (batchItem.getQuantityRemaining().compareTo(productDTO.getQuantity()) < 0) {
                        return ("Not enough stock for product: " + product.getProductName() +
                                " in batch expiring on " + productDTO.getExpirationDate());
                    }
                } else {
                    // New behavior - aggregate all batches with null expiry date
                    List<BatchArrivalItem> batchItems = batchArrivalItemRepository
                            .findByProductAndExpiryDateIsNull(product);

                    BigDecimal totalAvailable = batchItems.stream()
                            .map(BatchArrivalItem::getQuantityRemaining)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    if (totalAvailable.compareTo(productDTO.getQuantity()) < 0) {
                        return ("Not enough stock for product: " + product.getProductName() +
                                " in non-expiring batches (needed: " + productDTO.getQuantity() +
                                ", available: " + totalAvailable + ")");
                    }
                }
            }
        }

        // If we get here, everything is available - now process
        for (ProductRequestDTO productDTO : productRequestDTOS) {
            Product product = productRepository.findByBarcode(productDTO.getBarcode()).get();

            if (product.getIsComposite()) {
                List<ProductRecipe> ingredients = productRecipeRepository.findByFinalProduct(product);
                for (ProductRecipe ingredientRecipe : ingredients) {
                    ProductInUse ingredientStock = productInUseRepository.findByProduct(ingredientRecipe.getIngredient()).get();
                    BigDecimal requiredVolume = ingredientRecipe.getQuantityRequired().multiply(productDTO.getQuantity());
                    ingredientStock.setVolumeRemaining(ingredientStock.getVolumeRemaining().subtract(requiredVolume));
                    productInUseRepository.save(ingredientStock);
                }
            } else {
                if (productDTO.getExpirationDate() != null) {
                    // Original behavior - exact match by expiry date
                    BatchArrivalItem batchItem = batchArrivalItemRepository.findByProductAndExpiryDate(product, productDTO.getExpirationDate()).get();
                    batchItem.setQuantityRemaining(batchItem.getQuantityRemaining().subtract(productDTO.getQuantity()));
                    batchArrivalItemRepository.save(batchItem);

                    if (batchItem.getQuantityRemaining().compareTo(batchItem.getProduct().getThreshold()) < 0) {
                        notificationService.sendLowStockNotification(
                                product.getProductName(),
                                batchItem.getQuantityRemaining().intValue(),
                                batchItem.getProduct().getThreshold()
                        );
                    }
                } else {
                    // New behavior - deduct from batches with null expiry date (in order of arrival or ID)
                    List<BatchArrivalItem> batchItems = batchArrivalItemRepository
                            .findByProductAndExpiryDateIsNullOrderByBatchItemIdAsc(product);

                    BigDecimal remainingQuantityNeeded = productDTO.getQuantity();
                    BigDecimal remainingQuantity = BigDecimal.valueOf(0);

                    for (BatchArrivalItem batchItem : batchItems) {
                        if (remainingQuantityNeeded.compareTo(BigDecimal.ZERO) <= 0) {
                            break;
                        }

                        BigDecimal quantityToDeduct = batchItem.getQuantityRemaining().min(remainingQuantityNeeded);
                        batchItem.setQuantityRemaining(batchItem.getQuantityRemaining().subtract(quantityToDeduct));
                        batchArrivalItemRepository.save(batchItem);

                        remainingQuantityNeeded = remainingQuantityNeeded.subtract(quantityToDeduct);
                    }

                    // Check for low stock notification
                    for (BatchArrivalItem batchItem : batchItems) {
                        remainingQuantity = remainingQuantity.add(batchItem.getQuantityRemaining());
                    }

                    System.out.println(remainingQuantity);
                    if (remainingQuantity.compareTo(batchItems.get(0).getProduct().getThreshold()) < 0) {
                        notificationService.sendLowStockNotification(
                                product.getProductName(),
                                remainingQuantity.intValue(),
                                batchItems.get(0).getProduct().getThreshold()
                        );
                    }
                }
            }

            totalAmount = totalAmount.add(product.getPrice().multiply(productDTO.getQuantity()));
            responseMessage.append("Processed sale for product: ").append(product.getProductName()).append("\n");
        }

        salesTransaction.setTotalAmount(totalAmount);
        salesTransaction.setStatus("COMPLETED");
        salesTransactionRepository.save(salesTransaction);

        return responseMessage.toString();
    }

    @Transactional
    public String cancelCheck(Long transactionId) {

        // Attempt to find the sales transaction by ID, or throw 404 if not found
        SalesTransaction salesTransaction = salesTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "SalesTransaction not found with ID: " + transactionId
                ));

        // Find and delete all sales items associated with this transaction
        List<SalesItem> salesItems = salesItemRepository.findBySalesTransaction(salesTransaction);
        salesItemRepository.deleteAll(salesItems);

        // Set the transaction total amount to zero and update its status to CANCELED
        salesTransaction.setTotalAmount(BigDecimal.ZERO);
        salesTransaction.setStatus("CANCELED");

        salesTransactionRepository.save(salesTransaction);

        return "Sale transaction canceled successfully.";
    }

}
