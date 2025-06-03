package com.example.SmartInventorySystem.shared.service;

import com.example.SmartInventorySystem.batcharrivalitem.entity.BatchArrivalItem;
import com.example.SmartInventorySystem.batcharrivalitem.repository.BatchArrivalItemRepository;
import com.example.SmartInventorySystem.saleitem.repository.SalesItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockAnalysisService {

    private final BatchArrivalItemRepository batchArrivalItemRepository;

    public StockAnalysisService(BatchArrivalItemRepository batchArrivalItemRepository) {
        this.batchArrivalItemRepository = batchArrivalItemRepository;
    }

    @Autowired
    public StockAnalysisService(SalesItemRepository salesItemRepository, BatchArrivalItemRepository batchArrivalItemRepository) {
        this.batchArrivalItemRepository = batchArrivalItemRepository;
    }

    public List<BatchArrivalItem> getOldestBatchesWithLowRemaining() {
        return batchArrivalItemRepository.findOldestBatchesWithLowRemaining();
    }
}
