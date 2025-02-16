package com.example.SmartInventorySystem.controller;

import com.example.SmartInventorySystem.dto.ProductRequestDTO;
import com.example.SmartInventorySystem.service.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/process")
    public ResponseEntity<String> processSale(@RequestBody List<ProductRequestDTO> productRequestDTOS) {
        String result = saleService.processSaleProduct(productRequestDTOS);
        return ResponseEntity.ok(result);
    }
}
