package com.example.SmartInventorySystem.shared.service;


import com.example.SmartInventorySystem.saleitem.repository.SalesItemRepository;
import com.example.SmartInventorySystem.shared.ForecastClient;
import com.example.SmartInventorySystem.shared.dto.SaleRecord;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
@Service
public class SalesForecastService {

    private final SalesItemRepository salesRepository;
    private final ForecastClient forecastClient;

    public SalesForecastService(SalesItemRepository salesRepository, ForecastClient forecastClient) {
        this.salesRepository = salesRepository;
        this.forecastClient = forecastClient;
    }

    public String runForecastAnalysis() throws Exception {
        List<Object[]> rawData = salesRepository.fetchAggregatedSales();

        List<SaleRecord> records = rawData.stream()
                .map(obj -> new SaleRecord(
                        ((java.sql.Date) obj[0]).toLocalDate(),
                        ((Number) obj[1]).longValue(),
                        ((Number) obj[2]).longValue()
                ))
                .toList();

        return forecastClient.sendCsvToForecastAPI(records);
    }

}
