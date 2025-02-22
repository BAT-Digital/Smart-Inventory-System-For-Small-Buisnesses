package com.example.SmartInventorySystem.controller;

import com.example.SmartInventorySystem.dto.analysisdto.DailySalesTrendDto;
import com.example.SmartInventorySystem.dto.analysisdto.ProductProfitDto;
import com.example.SmartInventorySystem.service.SalesAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
public class SalesAnalysisController {

    private final SalesAnalysisService salesAnalysisService;

    public SalesAnalysisController(SalesAnalysisService salesAnalysisService) {
        this.salesAnalysisService = salesAnalysisService;
    }

    // Endpoint to retrieve daily sales trends
    @GetMapping("/daily-sales")
    public ResponseEntity<List<DailySalesTrendDto>> getDailySalesTrends() {
        List<DailySalesTrendDto> trends = salesAnalysisService.getDailySalesTrends();
        return ResponseEntity.ok(trends);
    }

    // Endpoint for product profit analysis (including net profit)
    @GetMapping("/product-profit")
    public ResponseEntity<List<ProductProfitDto>> getProductProfitAnalysis() {
        List<ProductProfitDto> profitDtos = salesAnalysisService.getProductProfitAnalysis();
        return ResponseEntity.ok(profitDtos);
    }
}
