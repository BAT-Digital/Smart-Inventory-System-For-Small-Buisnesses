package com.example.SmartInventorySystem.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastResponseDTO {
    private List<TopProductDTO> topProducts;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopProductDTO {
        private Long productId;
        private String productName;
        private String categoryName;
        private String supplierName;
        private BigDecimal forecasted_sales;
        private String peak_day;
        private BigDecimal peak_value;
        private BigDecimal currentStock;
        private BigDecimal restockNeeded;
    }
} 