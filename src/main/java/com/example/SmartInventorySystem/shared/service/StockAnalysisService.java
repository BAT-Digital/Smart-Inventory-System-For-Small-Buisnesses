package com.example.SmartInventorySystem.shared.service;

import com.example.SmartInventorySystem.batcharrivalitem.entity.BatchArrivalItem;
import com.example.SmartInventorySystem.product.entity.Product;
import com.example.SmartInventorySystem.batcharrivalitem.repository.BatchArrivalItemRepository;
import com.example.SmartInventorySystem.saleitem.repository.SalesItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StockAnalysisService {

    @Autowired
    private SalesItemRepository salesItemRepository;

    @Autowired
    private BatchArrivalItemRepository batchArrivalItemRepository;

    public List<Product> findFrequentlyOutOfStockProducts() {
        // Get products with zero remaining stock
        return batchArrivalItemRepository.findProductsWithZeroRemainingStock();
    }

    public List<Product> findExcessStockProducts() {
        // Get products with high remaining stock but low sales
        return batchArrivalItemRepository.findExcessStockProducts();
    }

    public List<BatchArrivalItem> findExpiringProducts(LocalDate thresholdDate) {
        return batchArrivalItemRepository.findByExpiryDateBefore(thresholdDate);
    }

    public List<BatchArrivalItem> getOldestBatchesWithLowRemaining() {
        return batchArrivalItemRepository.findOldestBatchesWithLowRemaining();
    }
}
