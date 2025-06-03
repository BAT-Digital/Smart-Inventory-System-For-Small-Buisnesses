package com.example.SmartInventorySystem.shared.service;


import com.example.SmartInventorySystem.category.entity.Category;
import com.example.SmartInventorySystem.product.entity.Product;
import com.example.SmartInventorySystem.batcharrivalitem.repository.BatchArrivalItemRepository;
import com.example.SmartInventorySystem.saleitem.repository.SalesItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InventoryAnalysisService {

    private SalesItemRepository salesItemRepository;

    private final BatchArrivalItemRepository batchArrivalItemRepository;

    public InventoryAnalysisService(BatchArrivalItemRepository batchArrivalItemRepository) {
        this.batchArrivalItemRepository = batchArrivalItemRepository;
    }

    @Autowired
    public InventoryAnalysisService(SalesItemRepository salesItemRepository, BatchArrivalItemRepository batchArrivalItemRepository) {
        this.salesItemRepository = salesItemRepository;
        this.batchArrivalItemRepository = batchArrivalItemRepository;
    }

    public BigDecimal calculateInventoryTurnoverRatio(LocalDateTime startDate, LocalDateTime endDate) {
        // Calculate COGS
        BigDecimal cogs = salesItemRepository.findTotalCOGS(startDate, endDate);

        // Calculate Average Inventory Value
        BigDecimal averageInventoryValue = batchArrivalItemRepository.findAverageInventoryValue(startDate, endDate);

        // Avoid division by zero
        if (averageInventoryValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return cogs.divide(averageInventoryValue, 2, RoundingMode.HALF_UP);
    }


    public Map<Category, BigDecimal> getCategoryPerformance() {
        List<Object[]> results = salesItemRepository.findSalesByCategory();
        Map<Category, BigDecimal> categorySalesMap = new HashMap<>();

        for (Object[] result : results) {
            Category category = (Category) result[0];
            BigDecimal totalSales = (BigDecimal) result[1];
            categorySalesMap.put(category, totalSales);
        }

        return categorySalesMap;
    }

    public List<Product> findTopPerformingProducts() {
        List<Object[]> productsWithRevenue = salesItemRepository.findProductsByRevenue();
        int totalProducts = productsWithRevenue.size();
        int top20PercentCount = (int) Math.ceil(totalProducts * 0.2);

        return productsWithRevenue.stream()
                .limit(top20PercentCount)
                .map(result -> (Product) result[0])
                .collect(Collectors.toList());
    }
}
