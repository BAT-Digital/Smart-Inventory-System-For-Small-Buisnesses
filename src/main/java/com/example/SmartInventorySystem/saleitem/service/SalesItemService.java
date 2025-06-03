package com.example.SmartInventorySystem.saleitem.service;

import com.example.SmartInventorySystem.product.entity.Product;

import com.example.SmartInventorySystem.product.repository.ProductRepository;

import com.example.SmartInventorySystem.saleitem.dto.SalesItemDTO;
import com.example.SmartInventorySystem.saleitem.entity.SalesItem;
import com.example.SmartInventorySystem.saleitem.repository.SalesItemRepository;
import com.example.SmartInventorySystem.salestransaction.entity.SalesTransaction;
import com.example.SmartInventorySystem.salestransaction.repository.SalesTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalesItemService {

    private final SalesItemRepository salesItemRepository;
    private final SalesTransactionRepository salesTransactionRepository;
    private final ProductRepository productRepository;

    public SalesItemService(SalesItemRepository salesItemRepository,
                            SalesTransactionRepository salesTransactionRepository,
                            ProductRepository productRepository) {
        this.salesItemRepository = salesItemRepository;
        this.salesTransactionRepository = salesTransactionRepository;
        this.productRepository = productRepository;
    }

    public List<SalesItem> getAllSalesItems() {
        return salesItemRepository.findAll();
    }

    public List<SalesItem> getSalesItemsByTransactionId(Long transactionId) {
        return salesItemRepository.findBySalesTransaction_TransactionId(transactionId);
    }

    public Optional<SalesItem> getSalesItemById(Long id) {
        return salesItemRepository.findById(id);
    }

    public SalesItem createSalesItemByDTO(SalesItemDTO salesItemDTO) {
        Optional<SalesItem> existingItem;
        if(salesItemDTO.getExpiryDate() == null) {
            existingItem = salesItemRepository.findByTransactionAndProduct(
                    salesItemDTO.getTransactionId(),
                    salesItemDTO.getProductId()
            );
        } else {
            // First try to find an existing item with the same transaction, product, and expiry date
            existingItem = salesItemRepository.findByTransactionAndProductAndExpiryDate(
                    salesItemDTO.getTransactionId(),
                    salesItemDTO.getProductId(),
                    salesItemDTO.getExpiryDate()
            );
        }

        if (existingItem.isPresent()) {
            // If found, increment the quantity
            SalesItem item = existingItem.get();
            item.setQuantity(item.getQuantity().add(salesItemDTO.getQuantity())); // Or add 1 if you always want +1
            return salesItemRepository.save(item);
        } else {
            // If not found, create new item
            SalesItem salesItem = new SalesItem();

            SalesTransaction salesTransaction = salesTransactionRepository.findById(salesItemDTO.getTransactionId())
                    .orElseThrow(() -> new RuntimeException("SalesTransaction not found with ID: " + salesItemDTO.getTransactionId()));
            salesItem.setSalesTransaction(salesTransaction);

            Product product = productRepository.findById(salesItemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + salesItemDTO.getProductId()));
            salesItem.setProduct(product);

            salesItem.setExpiryDate(salesItemDTO.getExpiryDate());
            salesItem.setQuantity(salesItemDTO.getQuantity());
            return salesItemRepository.save(salesItem);
        }
    }

    public SalesItem createSalesItem(SalesItem salesItem) {
        return salesItemRepository.save(salesItem);
    }

    public SalesItem updateSalesItem(Long id, SalesItem updatedSalesItem) {
        return salesItemRepository.findById(id).map(item -> {
            item.setSalesTransaction(updatedSalesItem.getSalesTransaction());
            item.setProduct(updatedSalesItem.getProduct());
            item.setQuantity(updatedSalesItem.getQuantity());
            return salesItemRepository.save(item);
        }).orElse(null);
    }

    public void deleteSalesItem(Long id) {
        salesItemRepository.deleteById(id);
    }

    public void deleteSalesItems(List<Long> ids) {
        for (Long id : ids) {
            salesItemRepository.deleteById(id);
        }
    }


}
