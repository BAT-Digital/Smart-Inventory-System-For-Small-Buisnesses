package com.example.SmartInventorySystem.service;

import com.example.SmartInventorySystem.model.BatchArrivalItem;
import com.example.SmartInventorySystem.model.Product;
import com.example.SmartInventorySystem.repository.crud.BatchArrivalItemRepository;
import com.example.SmartInventorySystem.repository.crud.SalesItemRepository;
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
}
