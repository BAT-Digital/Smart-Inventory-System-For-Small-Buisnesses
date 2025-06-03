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
            // Call service to generate generic test data
            testDataGeneratorService.generateTestData();
            // Return success response if generation completes without errors
            return ResponseEntity.ok("Test data successfully generated");
        } catch (Exception e) {
            // Return internal server error with exception message if generation fails
            return ResponseEntity.internalServerError()
                    .body("Error generating test data: " + e.getMessage());
        }
    }

    @PostMapping("/generate-ai-data")
    public ResponseEntity<String> generateAITestData() {
        try {
            // Call service to generate AI-specific test data
            testDataGeneratorService.generateAITestData();
            // Return success response upon completion
            return ResponseEntity.ok("Special AI test data successfully generated");
        } catch (Exception e) {
            // Return error response with details if AI data generation fails
            return ResponseEntity.internalServerError()
                    .body("Error generating AI test data: " + e.getMessage());
        }
    }

    @PostMapping("/generate-cafe-data")
    public ResponseEntity<String> generateCafeData() {
        try {
            // Call service to generate test data specific to cafe domain
            cafeTestDataGeneratorService.generateCafeData();
            // Return success message after data generation
            return ResponseEntity.ok("Cafe test data successfully generated!");
        } catch (Exception e) {
            // Return HTTP 500 with error message if generation fails
            return ResponseEntity.status(500).body("Error generating cafe test data: " + e.getMessage());
        }
    }

} 