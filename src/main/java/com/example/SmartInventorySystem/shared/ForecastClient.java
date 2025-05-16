package com.example.SmartInventorySystem.shared;

import com.example.SmartInventorySystem.shared.dto.SaleRecord;
import com.example.SmartInventorySystem.shared.model.NotificationTypes;
import com.example.SmartInventorySystem.shared.service.NotificationService;
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
import java.util.List;

@Component
public class ForecastClient {

    private  final NotificationService notificationService;

    public ForecastClient(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public String sendCsvToForecastAPI(List<SaleRecord> sales) throws Exception {
        // 1. Генерация CSV
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(out), CSVFormat.DEFAULT.withHeader("ds", "product_id", "y"))) {

            for (SaleRecord record : sales) {
                csvPrinter.printRecord(record.getDate(), record.getProductId(), record.getQuantity());
            }
            csvPrinter.flush();

            // 2. Создание HTTP-запроса
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Преобразуем CSV в ByteArrayResource
            ByteArrayResource fileAsResource = new ByteArrayResource(out.toByteArray()) {
                @Override
                public String getFilename() {
                    return "sales_data.csv";
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileAsResource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 3. Отправка на FastAPI
            RestTemplate restTemplate = new RestTemplate();
            String fastApiUrl = "http://host.docker.internal:8000/forecast";  // для Docker контейнеров

            ResponseEntity<String> response = restTemplate.postForEntity(fastApiUrl, requestEntity, String.class);
            notificationService.sendAICompletionNotification("Prediction results are ready go check it!", NotificationTypes.NotificationPriority.LOW);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody(); // JSON строка
            } else {
                throw new Exception("Ошибка при отправке данных на сервер FastAPI: " + response.getStatusCode());
            }
        } catch (IOException e) {
            throw new Exception("Ошибка при генерации CSV файла", e);
        }
    }
}
