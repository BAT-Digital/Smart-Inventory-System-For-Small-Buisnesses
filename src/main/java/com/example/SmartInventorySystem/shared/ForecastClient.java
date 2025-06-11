package com.example.SmartInventorySystem.shared;

import com.example.SmartInventorySystem.batcharrivalitem.repository.BatchArrivalItemRepository;
import com.example.SmartInventorySystem.product.entity.Product;
import com.example.SmartInventorySystem.product.repository.ProductRepository;
import com.example.SmartInventorySystem.shared.dto.ForecastResponseDTO;
import com.example.SmartInventorySystem.shared.dto.SaleRecord;
import com.example.SmartInventorySystem.shared.model.NotificationTypes;
import com.example.SmartInventorySystem.shared.service.NotificationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ForecastClient {

    private final NotificationService notificationService;
    private final ProductRepository productRepository;
    private final BatchArrivalItemRepository batchArrivalItemRepository;
    private final ObjectMapper objectMapper;
    private final Map<Long, Long> productIdMapping = new HashMap<>();

    public ForecastClient(NotificationService notificationService, 
                          ProductRepository productRepository,
                          BatchArrivalItemRepository batchArrivalItemRepository) {
        this.notificationService = notificationService;
        this.productRepository = productRepository;
        this.batchArrivalItemRepository = batchArrivalItemRepository;
        this.objectMapper = new ObjectMapper();
    }

    public ForecastResponseDTO sendCsvToForecastAPI(List<SaleRecord> sales) throws Exception {
        // 1. Generate CSV
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(out), CSVFormat.DEFAULT.withHeader("ds", "product_id", "y"))) {

            for (SaleRecord record : sales) {
                csvPrinter.printRecord(record.getDate(), record.getProductId(), record.getQuantity());
            }
            csvPrinter.flush();

            // 2. Create HTTP request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Convert CSV to ByteArrayResource
            ByteArrayResource fileAsResource = new ByteArrayResource(out.toByteArray()) {
                @Override
                public String getFilename() {
                    return "sales_data.csv";
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileAsResource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 3. Send to FastAPI
            try {
                RestTemplate restTemplate = new RestTemplate();
                String fastApiUrl = "http://host.docker.internal:8000/forecast";  // for Docker containers
                
                System.out.println("Sending request to ML service at: " + fastApiUrl);
                System.out.println("CSV data contains " + sales.size() + " records");
                
                ResponseEntity<String> response = restTemplate.postForEntity(fastApiUrl, requestEntity, String.class);
                System.out.println("ML service response status: " + response.getStatusCode());
                
                // Вывод ответа для отладки
                if (response.getBody() != null) {
                    System.out.println("Response body length: " + response.getBody().length() + " characters");
                } else {
                    System.out.println("Response body is null");
                }
                
                if (response.getStatusCode() == HttpStatus.OK) {
                    // 4. Process and enhance the response
                    return processAndEnhanceResponse(response.getBody());
                } else {
                    throw new Exception("Error when sending data to FastAPI server: " + response.getStatusCode());
                }
            } catch (Exception e) {
                System.out.println("Error communicating with ML service: " + e.getMessage());
                e.printStackTrace();
                throw new Exception("Failed to get forecast from ML service: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error generating CSV file: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Error generating CSV file: " + e.getMessage());
        }
    }
    
    private ForecastResponseDTO processAndEnhanceResponse(String jsonResponse) {
        try {
            System.out.println("Raw JSON response from ML service:");
            System.out.println(jsonResponse);

            if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
                System.out.println("Received empty JSON response from ML service");
                return ForecastResponseDTO.builder()
                        .topProducts(new ArrayList<>())
                        .build();
            }
            
            JsonNode rootNode;
            try {
                rootNode = objectMapper.readTree(jsonResponse);
            } catch (Exception e) {
                System.out.println("Failed to parse JSON response: " + e.getMessage());
                return ForecastResponseDTO.builder()
                        .topProducts(new ArrayList<>())
                        .build();
            }

            JsonNode topProductsNode = rootNode.get("top_products");
            if (topProductsNode == null) {
                System.out.println("JSON response does not contain 'top_products' field");
                if (rootNode.isArray()) {
                    System.out.println("JSON response is an array, using it directly as top products");
                    topProductsNode = rootNode;
                } else {
                    System.out.println("Available fields in JSON response:");
                    rootNode.fieldNames().forEachRemaining(name -> 
                        System.out.println(" - " + name));
                    
                    return ForecastResponseDTO.builder()
                            .topProducts(new ArrayList<>())
                            .build();
                }
            }
            
            System.out.println("=== Starting product diagnostics ===");
            List<Long> allMlProductIds = new ArrayList<>();
            List<Long> allDbProductIds = new ArrayList<>();
            for (JsonNode productNode : topProductsNode) {
                try {
                    Long mlProductId = productNode.get("product_id").asLong();
                    Long dbProductId = productIdMapping.getOrDefault(mlProductId, mlProductId);
                    
                    allMlProductIds.add(mlProductId);
                    allDbProductIds.add(dbProductId);
                    
                    System.out.println("Forecast product: ML ID=" + mlProductId + ", mapped to DB ID=" + dbProductId);
                } catch (Exception e) {
                    System.out.println("Error reading product ID: " + e.getMessage());
                }
            }

            if (!allDbProductIds.isEmpty()) {
                List<Product> existingProducts = productRepository.findAllById(allDbProductIds);
                System.out.println("Found " + existingProducts.size() + " products out of " + allDbProductIds.size());

                Map<Long, String> foundProductNames = new HashMap<>();
                for (Product product : existingProducts) {
                    System.out.println("Found product: ID=" + product.getProductId() + ", Name=" + product.getProductName());
                    foundProductNames.put(product.getProductId(), product.getProductName());
                }

                List<Long> foundIds = existingProducts.stream().map(Product::getProductId).toList();
                for (int i = 0; i < allDbProductIds.size(); i++) {
                    Long dbId = allDbProductIds.get(i);
                    Long mlId = allMlProductIds.get(i);
                    
                    if (!foundIds.contains(dbId)) {
                        System.out.println("Product with ML ID=" + mlId + ", DB ID=" + dbId + " was NOT found in database");

                        Optional<Product> product = productRepository.findById(dbId);
                        if (product.isPresent()) {
                            System.out.println("But findById found product: ID=" + product.get().getProductId() + ", Name=" + product.get().getProductName());
                            foundProductNames.put(dbId, product.get().getProductName());
                        } else {
                            System.out.println("findById also did not find product with DB ID " + dbId);

                            System.out.println("Searching for possible product matches...");
                            List<Product> allProducts = productRepository.findAll();
                            System.out.println("Total products in database: " + allProducts.size());
                            for (Product p : allProducts) {
                                System.out.println("Available product: ID=" + p.getProductId() + ", Name=" + p.getProductName());
                            }
                        }
                    }
                }
            }
            System.out.println("=== End of product diagnostics ===");
            
            List<ForecastResponseDTO.TopProductDTO> enhancedProducts = new ArrayList<>();
            StringBuilder notificationMessage = new StringBuilder("Forecast results: Top products that need restocking:\n");
            
            for (JsonNode productNode : topProductsNode) {
                try {
                    Long mlProductId = productNode.get("product_id").asLong();

                    Long dbProductId = productIdMapping.getOrDefault(mlProductId, mlProductId);
                    System.out.println("Processing product: ML ID=" + mlProductId + ", DB ID=" + dbProductId);
                    
                    BigDecimal forecastedSales = new BigDecimal(productNode.get("forecasted_sales").asText());
                    String peakDay = productNode.get("peak_day").asText();
                    BigDecimal peakValue = new BigDecimal(productNode.get("peak_value").asText());

                    // Get additional product information from database using mapped ID
                    Optional<Product> productOpt = productRepository.findById(dbProductId);
                    
                    if (productOpt.isPresent()) {
                        Product product = productOpt.get();
                        
                        // Get current stock level
                        BigDecimal currentStock = null;
                        try {
                            currentStock = batchArrivalItemRepository.findRemainingStockByProduct(dbProductId);
                        } catch (Exception e) {
                            System.out.println("Error getting stock for product ID " + dbProductId + ": " + e.getMessage());
                        }
                        
                        if (currentStock == null) {
                            currentStock = BigDecimal.ZERO;
                        }
                        
                        // Calculate restocking needs (forecasted sales - current stock)
                        BigDecimal restockNeeded = forecastedSales.subtract(currentStock);
                        if (restockNeeded.compareTo(BigDecimal.ZERO) < 0) {
                            restockNeeded = BigDecimal.ZERO;
                        }
                        
                        // Set precision
                        restockNeeded = restockNeeded.setScale(2, RoundingMode.HALF_UP);
                        
                        // Build enhanced product DTO
                        ForecastResponseDTO.TopProductDTO enhancedProduct = ForecastResponseDTO.TopProductDTO.builder()
                                .productId(dbProductId)
                                .productName(product.getProductName())
                                .categoryName(product.getCategory() != null ? product.getCategory().getName() : "Uncategorized")
                                .supplierName(product.getSupplier() != null ? product.getSupplier().getName() : "Unknown")
                                .forecasted_sales(forecastedSales)
                                .peak_day(peakDay)
                                .peak_value(peakValue)
                                .currentStock(currentStock)
                                .restockNeeded(restockNeeded)
                                .build();
                        
                        enhancedProducts.add(enhancedProduct);
                        
                        // Add to notification message if restocking is needed
                        if (restockNeeded.compareTo(BigDecimal.ZERO) > 0) {
                            notificationMessage.append("- ")
                                    .append(product.getProductName())
                                    .append(": ")
                                    .append(restockNeeded)
                                    .append(" ")
                                    .append(product.getUnitOfMeasure())
                                    .append("\n");
                        }
                    } else {
                        // Product not found in database, create a placeholder
                        System.out.println("Product with ID " + dbProductId + " not found in database");
                        
                        ForecastResponseDTO.TopProductDTO placeholderProduct = ForecastResponseDTO.TopProductDTO.builder()
                                .productId(dbProductId)
                                .productName("Product #" + dbProductId + " (Not Found)")
                                .categoryName("Unknown")
                                .supplierName("Unknown")
                                .forecasted_sales(forecastedSales)
                                .peak_day(peakDay)
                                .peak_value(peakValue)
                                .currentStock(BigDecimal.ZERO)
                                .restockNeeded(forecastedSales)
                                .build();
                        
                        enhancedProducts.add(placeholderProduct);
                    }
                } catch (Exception e) {
                    System.out.println("Error processing product node: " + e.getMessage());
                    e.printStackTrace();
                    // Continue processing other products
                }
            }
            
            if (enhancedProducts.isEmpty()) {
                System.out.println("No enhanced products were created. Response might be empty or invalid.");
            }
            
            // Send notification with restock recommendations if we have any products
            if (!enhancedProducts.isEmpty()) {
                try {
                    notificationService.sendAICompletionNotification(
                            notificationMessage.toString(),
                            NotificationTypes.NotificationPriority.NORMAL
                    );
                } catch (Exception e) {
                    System.out.println("Error sending notification: " + e.getMessage());
                    // Don't let notification failure prevent returning results
                }
            }
            
            return ForecastResponseDTO.builder()
                    .topProducts(enhancedProducts)
                    .build();
        } catch (Exception e) {
            System.out.println("Error in processAndEnhanceResponse: " + e.getMessage());
            e.printStackTrace();
            // Return empty response rather than throwing exception
            return ForecastResponseDTO.builder()
                    .topProducts(new ArrayList<>())
                    .build();
        }
    }
}
