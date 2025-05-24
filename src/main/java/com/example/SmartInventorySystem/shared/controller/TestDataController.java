package com.example.SmartInventorySystem.shared.controller;

import com.example.SmartInventorySystem.shared.service.TestDataGeneratorService;
import com.example.SmartInventorySystem.shared.service.CafeTestDataGeneratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-data")
@CrossOrigin(origins = "*", allowCredentials = "false")
public class TestDataController {

    private final TestDataGeneratorService testDataGeneratorService;
    private final CafeTestDataGeneratorService cafeTestDataGeneratorService;

    public TestDataController(TestDataGeneratorService testDataGeneratorService, CafeTestDataGeneratorService cafeTestDataGeneratorService) {
        this.testDataGeneratorService = testDataGeneratorService;
        this.cafeTestDataGeneratorService = cafeTestDataGeneratorService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateTestData() {
        try {
            testDataGeneratorService.generateTestData();
            return ResponseEntity.ok("Тестовые данные успешно сгенерированы");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Ошибка при генерации тестовых данных: " + e.getMessage());
        }
    }
    
    @PostMapping("/generate-ai-data")
    public ResponseEntity<String> generateAITestData() {
        try {
            testDataGeneratorService.generateAITestData();
            return ResponseEntity.ok("Специальные тестовые данные для AI успешно сгенерированы");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Ошибка при генерации тестовых данных для AI: " + e.getMessage());
        }
    }

    @PostMapping("/generate-cafe-data")
    public ResponseEntity<String> generateCafeData() {
        try {
            cafeTestDataGeneratorService.generateCafeData();
            return ResponseEntity.ok("Cafe test data successfully generated!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating cafe test data: " + e.getMessage());
        }
    }
} 