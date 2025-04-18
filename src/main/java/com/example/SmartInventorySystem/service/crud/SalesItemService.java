package com.example.SmartInventorySystem.service.crud;

import com.example.SmartInventorySystem.dto.SalesItemDTO;
import com.example.SmartInventorySystem.model.Product;
import com.example.SmartInventorySystem.model.SalesItem;
import com.example.SmartInventorySystem.model.SalesTransaction;
import com.example.SmartInventorySystem.repository.crud.ProductRepository;
import com.example.SmartInventorySystem.repository.crud.SalesItemRepository;
import com.example.SmartInventorySystem.repository.crud.SalesTransactionRepository;
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
        SalesItem salesItem = new SalesItem();

        // Fetch the associated SalesTransaction
        SalesTransaction salesTransaction = salesTransactionRepository.findById(salesItemDTO.getTransactionId())
                .orElseThrow(() -> new RuntimeException("SalesTransaction not found with ID: " + salesItemDTO.getTransactionId()));
        salesItem.setSalesTransaction(salesTransaction);

        // Fetch the associated Product
        Product product = productRepository.findById(salesItemDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + salesItemDTO.getProductId()));
        salesItem.setProduct(product);

        // Set the quantity
        salesItem.setQuantity(salesItemDTO.getQuantity());
        return salesItemRepository.save(salesItem);
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


}
