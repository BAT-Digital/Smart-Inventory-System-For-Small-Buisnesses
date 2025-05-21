package com.example.SmartInventorySystem.shared.controller;

import com.example.SmartInventorySystem.shared.service.TestDataGeneratorService;
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

    public TestDataController(TestDataGeneratorService testDataGeneratorService) {
        this.testDataGeneratorService = testDataGeneratorService;
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
} 