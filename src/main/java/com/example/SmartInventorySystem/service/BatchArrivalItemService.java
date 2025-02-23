package com.example.SmartInventorySystem.service;

import com.example.SmartInventorySystem.dto.BatchArrivalItemDTO;
import com.example.SmartInventorySystem.model.BatchArrival;
import com.example.SmartInventorySystem.model.BatchArrivalItem;
import com.example.SmartInventorySystem.model.Product;
import com.example.SmartInventorySystem.repository.crud.BatchArrivalItemRepository;
import com.example.SmartInventorySystem.repository.crud.BatchArrivalRepository;
import com.example.SmartInventorySystem.repository.crud.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BatchArrivalItemService {

    private final BatchArrivalRepository batchArrivalRepository;
    private final ProductRepository productRepository;
    private final BatchArrivalItemRepository batchArrivalItemRepository;

    public BatchArrivalItemService(BatchArrivalRepository batchArrivalRepository,
                                   ProductRepository productRepository,
                                   BatchArrivalItemRepository batchArrivalItemRepository) {
        this.batchArrivalRepository = batchArrivalRepository;
        this.productRepository = productRepository;
        this.batchArrivalItemRepository = batchArrivalItemRepository;
    }

    @Transactional
    public String processBatchArrivalItems(List<BatchArrivalItemDTO> batchArrivalItemDTOs) {
        StringBuilder responseMessage = new StringBuilder();
        List<BatchArrivalItem> batchArrivalItems = new ArrayList<>();

        for (BatchArrivalItemDTO itemDTO : batchArrivalItemDTOs) {
            Optional<BatchArrival> batchArrivalOpt = batchArrivalRepository.findById(itemDTO.getBatchArrivalId());
            Optional<Product> productOpt = productRepository.findById(itemDTO.getProductId());

            if (batchArrivalOpt.isPresent() && productOpt.isPresent()) {
                BatchArrival batchArrival = batchArrivalOpt.get();
                Product product = productOpt.get();

                BatchArrivalItem batchArrivalItem = new BatchArrivalItem();
                batchArrivalItem.setBatchArrival(batchArrival);
                batchArrivalItem.setProduct(product);
                batchArrivalItem.setQuantityReceived(itemDTO.getQuantityReceived());
                batchArrivalItem.setQuantityRemaining(itemDTO.getQuantityReceived());
                batchArrivalItem.setExpiryDate(itemDTO.getExpiryDate());
                batchArrivalItem.setUnitCost(itemDTO.getUnitCost());
                batchArrivalItems.add(batchArrivalItem);

                responseMessage.append("Processed BatchArrivalItem for product: ")
                        .append(product.getProductName())
                        .append("\n");
            } else {
                responseMessage.append("Failed to process BatchArrivalItem: ");
                if (batchArrivalOpt.isEmpty()) {
                    responseMessage.append("BatchArrival not found with ID: ").append(itemDTO.getBatchArrivalId()).append(". ");
                }
                if (productOpt.isEmpty()) {
                    responseMessage.append("Product not found with ID: ").append(itemDTO.getProductId()).append(". ");
                }
                responseMessage.append("\n");
            }
        }

        batchArrivalItemRepository.saveAll(batchArrivalItems);
        return responseMessage.toString();
    }

    public List<BatchArrivalItem> getBatchArrivalItemsByArrivalId(Long arrivalId) {
        return batchArrivalItemRepository.findByBatchArrivalArrivalId(arrivalId);
    }

    public List<BatchArrivalItem> getAllBatchArrivalItems() {
        return batchArrivalItemRepository.findAll();
    }

    public Optional<BatchArrivalItem> getBatchArrivalItemById(Long id) {
        return batchArrivalItemRepository.findById(id);
    }

    public BatchArrivalItem updateBatchArrivalItem(Long id, BatchArrivalItem updatedBatchArrivalItem) {
        return batchArrivalItemRepository.findById(id).map(item -> {
            item.setBatchArrival(updatedBatchArrivalItem.getBatchArrival());
            item.setProduct(updatedBatchArrivalItem.getProduct());
            item.setQuantityReceived(updatedBatchArrivalItem.getQuantityReceived());
            item.setQuantityRemaining(updatedBatchArrivalItem.getQuantityRemaining());
            item.setExpiryDate(updatedBatchArrivalItem.getExpiryDate());
            item.setUnitCost(updatedBatchArrivalItem.getUnitCost());
            return batchArrivalItemRepository.save(item);
        }).orElse(null);
    }

    public void deleteBatchArrivalItem(Long id) {
        batchArrivalItemRepository.deleteById(id);
    }
}
