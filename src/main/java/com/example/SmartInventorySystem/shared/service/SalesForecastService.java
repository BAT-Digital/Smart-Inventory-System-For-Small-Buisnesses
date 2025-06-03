package com.example.SmartInventorySystem.shared.service;


import com.example.SmartInventorySystem.saleitem.repository.SalesItemRepository;
import com.example.SmartInventorySystem.shared.ForecastClient;
import com.example.SmartInventorySystem.shared.dto.ForecastResponseDTO;
import com.example.SmartInventorySystem.shared.dto.SaleRecord;
import lombok.Getter;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class SalesForecastService {

    private final SalesItemRepository salesRepository;
    private final ForecastClient forecastClient;
    @Getter
    private ForecastResponseDTO latestForecast;

    public SalesForecastService(SalesItemRepository salesRepository, ForecastClient forecastClient) {
        this.salesRepository = salesRepository;
        this.forecastClient = forecastClient;
    }

    public ForecastResponseDTO runForecastAnalysis() throws Exception {
        try {
            List<Object[]> rawData = salesRepository.fetchAggregatedSales();
            
            if (rawData == null || rawData.isEmpty()) {
                System.out.println("No sales data found for forecasting");
                throw new Exception("No sales data available for forecasting");
            }
            
            System.out.println("Found " + rawData.size() + " sales records for forecasting");

            List<SaleRecord> records = new ArrayList<>();
            
            for (Object[] obj : rawData) {
                try {
                    java.sql.Date date = (java.sql.Date) obj[0];
                    Number productId = (Number) obj[1];
                    Number quantity = (Number) obj[2];
                    
                    if (date != null && productId != null && quantity != null) {
                        records.add(new SaleRecord(
                            date.toLocalDate(),
                            productId.longValue(),
                            quantity.longValue()
                        ));
                    } else {
                        System.out.println("Skipping invalid record: date=" + date + ", productId=" + productId + ", quantity=" + quantity);
                    }
                } catch (Exception e) {
                    System.out.println("Error processing sales record: " + e.getMessage());
                    // Continue with next record
                }
            }
            
            if (records.isEmpty()) {
                System.out.println("No valid sales records after processing");
                throw new Exception("No valid sales records available for forecasting");
            }
            
            System.out.println("Processed " + records.size() + " valid sales records for forecasting");
            
            latestForecast = forecastClient.sendCsvToForecastAPI(records);
            return latestForecast;
        } catch (Exception e) {
            System.out.println("Error in runForecastAnalysis: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

}
