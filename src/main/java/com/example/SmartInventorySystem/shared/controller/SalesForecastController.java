package com.example.SmartInventorySystem.shared.controller;

import com.example.SmartInventorySystem.shared.service.SalesForecastService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/forecast")
public class SalesForecastController {

    private final SalesForecastService forecastService;

    public SalesForecastController(SalesForecastService forecastService) {
        this.forecastService = forecastService;
    }
    @PostMapping
    public ResponseEntity<String> forecast() {
        try {
            String result = forecastService.runForecastAnalysis();
            return ResponseEntity.ok(result); // или JSON → Map → DTO
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка анализа: " + e.getMessage());
        }
    }
}
