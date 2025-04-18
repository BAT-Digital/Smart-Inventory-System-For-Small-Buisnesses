package com.example.SmartInventorySystem.service;

import com.example.SmartInventorySystem.repository.crud.WriteOffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WriteOffAnalysisService {

    @Autowired
    private WriteOffRepository writeOffRepository;

    public BigDecimal calculateCostOfWaste() {
        return writeOffRepository.findTotalCostOfWaste();
    }
}
