package com.example.SmartInventorySystem.shared.controller;

import com.example.SmartInventorySystem.batcharrivalitem.entity.BatchArrivalItem;

import com.example.SmartInventorySystem.category.entity.Category;
import com.example.SmartInventorySystem.product.entity.Product;
import com.example.SmartInventorySystem.batcharrivalitem.repository.BatchArrivalItemRepository;
import com.example.SmartInventorySystem.saleitem.repository.SalesItemRepository;
import com.example.SmartInventorySystem.shared.service.InventoryAnalysisService;
import com.example.SmartInventorySystem.shared.service.SalesTrendAnalysisService;
import com.example.SmartInventorySystem.shared.service.StockAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private InventoryAnalysisService inventoryService;

    @Autowired
    private SalesTrendAnalysisService salesTrendAnalysisService;

    @Autowired
    private SalesItemRepository salesItemRepository;

    @Autowired
    private BatchArrivalItemRepository batchArrivalItemRepository;

    @Autowired
    private StockAnalysisService stockAnalysisService;

    @GetMapping("/inventory-turnover")
    public BigDecimal getInventoryTurnoverRatio() {
        return inventoryService.calculateInventoryTurnoverRatio(LocalDateTime.now().minusMonths(1), LocalDateTime.now());
    }

    @GetMapping("/sales-trends")
    public Map<LocalDate, BigDecimal> getSalesTrends() {
        return salesTrendAnalysisService.getSalesTrends(LocalDateTime.now().minusMonths(1), LocalDateTime.now());
    }

    @GetMapping("/product-performance/{productId}")
    public Map<String, Object> getProductPerformance(@PathVariable Long productId) {
        Map<String, Object> performanceData = new HashMap<>();
        performanceData.put("sales", salesItemRepository.findTotalSalesByProduct(productId));
        performanceData.put("stock", batchArrivalItemRepository.findRemainingStockByProduct(productId));
        return performanceData;
    }

    @GetMapping("/category-performance")
    public Map<Category, BigDecimal> getCategoryPerformance() {
        return inventoryService.getCategoryPerformance();
    }

    @GetMapping("/top-performing-products")
    public List<Product> findTopPerformingProducts() {
        return inventoryService.findTopPerformingProducts();
    }

    //For Recommended to buy
    @GetMapping("/oldest-low-remaining-batches")
    public List<BatchArrivalItem> getOldestBatchesWithLowRemaining() {
        return stockAnalysisService.getOldestBatchesWithLowRemaining();
    }
}
