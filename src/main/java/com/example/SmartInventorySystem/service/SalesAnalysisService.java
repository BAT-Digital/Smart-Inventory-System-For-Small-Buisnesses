package com.example.SmartInventorySystem.service;

import com.example.SmartInventorySystem.dto.analysisdto.DailySalesTrendDto;
import com.example.SmartInventorySystem.dto.analysisdto.ProductProfitDto;
import com.example.SmartInventorySystem.repository.crud.BatchArrivalItemRepository;
import com.example.SmartInventorySystem.repository.crud.SalesItemRepository;
import com.example.SmartInventorySystem.repository.crud.SalesTransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SalesAnalysisService {

    private final SalesTransactionRepository salesTransactionRepository;
    private final SalesItemRepository salesItemRepository;
    private final BatchArrivalItemRepository batchArrivalItemRepository;

    public SalesAnalysisService(SalesTransactionRepository salesTransactionRepository,
                                SalesItemRepository salesItemRepository,
                                BatchArrivalItemRepository batchArrivalItemRepository) {
        this.salesTransactionRepository = salesTransactionRepository;
        this.salesItemRepository = salesItemRepository;
        this.batchArrivalItemRepository = batchArrivalItemRepository;
    }

    public List<DailySalesTrendDto> getDailySalesTrends() {
        // 1. Retrieve daily revenue and transaction counts.
        List<Object[]> dailyRevenueData = salesTransactionRepository.findDailySalesTrends();
        // Map date to a DTO holding revenue and transaction count.
        Map<LocalDate, DailySalesTrendDto> dailyMap = new HashMap<>();
        for (Object[] row : dailyRevenueData) {
            LocalDate date = ((Date) row[0]).toLocalDate();
            BigDecimal totalRevenue = (BigDecimal) row[1];
            Long transactionCount = (Long) row[2];
            // Initialize net profit to totalRevenue (will subtract cost later)
            dailyMap.put(date, new DailySalesTrendDto(date, totalRevenue, transactionCount, totalRevenue));
        }

        // 2. Retrieve average unit cost per product.
        List<Object[]> costResults = batchArrivalItemRepository.findAverageUnitCostPerProduct();
        Map<Long, BigDecimal> avgCostMap = new HashMap<>();
        for (Object[] row : costResults) {
            Long productId = (Long) row[0];
            BigDecimal avgCost = (BigDecimal) row[1];
            avgCostMap.put(productId, avgCost);
        }

        // 3. Retrieve daily product sales (units sold per product per day)
        List<Object[]> dailyProductSales = salesItemRepository.findDailyProductSales();
        // Map each day to the total cost of goods sold.
        Map<LocalDate, BigDecimal> dailyCostMap = new HashMap<>();
        for (Object[] row : dailyProductSales) {
            LocalDate date = ((Date) row[0]).toLocalDate();
            Long productId = (Long) row[1];
            BigDecimal unitsSold = (BigDecimal) row[2];
            // Look up the average unit cost for this product (default to ZERO if missing)
            BigDecimal avgCost = avgCostMap.getOrDefault(productId, BigDecimal.ZERO);
            // Calculate cost for this product sale
            BigDecimal costForProduct = unitsSold.multiply(avgCost);
            // Sum up the cost per day
            dailyCostMap.merge(date, costForProduct, BigDecimal::add);
        }

        // 4. Adjust net profit in the dailyMap: netProfit = totalRevenue - dailyCost
        for (Map.Entry<LocalDate, DailySalesTrendDto> entry : dailyMap.entrySet()) {
            LocalDate date = entry.getKey();
            DailySalesTrendDto dto = entry.getValue();
            BigDecimal totalCost = dailyCostMap.getOrDefault(date, BigDecimal.ZERO);
            BigDecimal netProfit = dto.getTotalRevenue().subtract(totalCost);
            // Update the DTO with the computed net profit
            dto.setNetProfit(netProfit);
        }

        return new ArrayList<>(dailyMap.values());
    }

    public List<ProductProfitDto> getProductProfitAnalysis() {
        // Retrieve product performance data
        List<Object[]> performanceResults = salesItemRepository.findProductPerformance();
        // Retrieve average cost data per product
        List<Object[]> costResults = batchArrivalItemRepository.findAverageUnitCostPerProduct();

        // Create a mapping from productId to average unit cost
        Map<Long, BigDecimal> costMap = new HashMap<>();
        for (Object[] row : costResults) {
            Long productId = (Long) row[0];
            BigDecimal avgCost = (BigDecimal) row[1];
            costMap.put(productId, avgCost);
        }

        List<ProductProfitDto> profitDtos = new ArrayList<>();
        for (Object[] row : performanceResults) {
            Long productId = (Long) row[0];
            BigDecimal unitsSold = (BigDecimal) row[1];
            BigDecimal revenue = (BigDecimal) row[2];

            // Get the average unit cost; default to ZERO if not available
            BigDecimal averageUnitCost = costMap.getOrDefault(productId, BigDecimal.ZERO);
            // Calculate total cost for sold units
            BigDecimal totalCost = unitsSold.multiply(averageUnitCost);
            // Compute net profit as revenue minus total cost
            BigDecimal netProfit = revenue.subtract(totalCost);

            profitDtos.add(new ProductProfitDto(productId, unitsSold, revenue, averageUnitCost, netProfit));
        }
        return profitDtos;
    }
}

