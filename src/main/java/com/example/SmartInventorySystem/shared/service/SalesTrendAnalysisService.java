package com.example.SmartInventorySystem.shared.service;

import com.example.SmartInventorySystem.saleitem.repository.SalesItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class SalesTrendAnalysisService {

    @Autowired
    private SalesItemRepository salesItemRepository;

    public Map<LocalDate, BigDecimal> getSalesTrends(LocalDateTime startDate, LocalDateTime endDate) {
        return salesItemRepository.findSalesTrends(startDate, endDate);
    }
}
