package com.example.SmartInventorySystem.shared.service;

import com.example.SmartInventorySystem.saleitem.repository.SalesItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class SalesTrendAnalysisService {

    private final SalesItemRepository salesItemRepository;

    public SalesTrendAnalysisService(SalesItemRepository salesItemRepository) {
        this.salesItemRepository = salesItemRepository;
    }

    public Map<LocalDate, BigDecimal> getSalesTrends(LocalDateTime startDate, LocalDateTime endDate) {
        return salesItemRepository.findSalesTrends(startDate, endDate);
    }
}
