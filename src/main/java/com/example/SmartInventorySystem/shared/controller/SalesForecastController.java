package com.example.SmartInventorySystem.shared.controller;

import com.example.SmartInventorySystem.shared.dto.ForecastResponseDTO;
import com.example.SmartInventorySystem.shared.service.SalesForecastService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forecast")
@CrossOrigin(origins = "*", allowCredentials = "false")
public class SalesForecastController {

    private final SalesForecastService forecastService;

    public SalesForecastController(SalesForecastService forecastService) {
        this.forecastService = forecastService;
    }
    
    @PostMapping
    public ResponseEntity<ForecastResponseDTO> forecast() {
        try {
            ForecastResponseDTO result = forecastService.runForecastAnalysis();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println("Error in forecast controller: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    
    @GetMapping
    public ResponseEntity<ForecastResponseDTO> getLatestForecast() {
        try {
            ForecastResponseDTO result = forecastService.getLatestForecast();
            if (result == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
