package com.example.SmartInventorySystem.service.crud;

import com.example.SmartInventorySystem.model.SalesItem;
import com.example.SmartInventorySystem.repository.crud.SalesItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalesItemService {

    private final SalesItemRepository salesItemRepository;

    public SalesItemService(SalesItemRepository salesItemRepository) {
        this.salesItemRepository = salesItemRepository;
    }

    public List<SalesItem> getAllSalesItems() {
        return salesItemRepository.findAll();
    }

    public Optional<SalesItem> getSalesItemById(Long id) {
        return salesItemRepository.findById(id);
    }

    public SalesItem createSalesItem(SalesItem salesItem) {
        return salesItemRepository.save(salesItem);
    }

    public SalesItem updateSalesItem(Long id, SalesItem updatedSalesItem) {
        return salesItemRepository.findById(id).map(item -> {
            item.setSalesTransaction(updatedSalesItem.getSalesTransaction());
            item.setProduct(updatedSalesItem.getProduct());
            item.setQuantity(updatedSalesItem.getQuantity());
            return salesItemRepository.save(item);
        }).orElse(null);
    }

    public void deleteSalesItem(Long id) {
        salesItemRepository.deleteById(id);
    }


}
