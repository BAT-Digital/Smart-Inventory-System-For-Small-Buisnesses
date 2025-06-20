package com.example.SmartInventorySystem.batcharrivalitem.service;

import com.example.SmartInventorySystem.batcharrival.entity.BatchArrival;
import com.example.SmartInventorySystem.batcharrivalitem.repository.BatchArrivalItemRepository;
import com.example.SmartInventorySystem.batcharrivalitem.dto.BatchArrivalItemDTO;
import com.example.SmartInventorySystem.batcharrivalitem.entity.BatchArrivalItem;
import com.example.SmartInventorySystem.product.entity.Product;
import com.example.SmartInventorySystem.batcharrival.repository.BatchArrivalRepository;
import com.example.SmartInventorySystem.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

        try {
            for (BatchArrivalItemDTO itemDTO : batchArrivalItemDTOs) {
                Optional<BatchArrival> batchArrivalOpt = batchArrivalRepository.findById(itemDTO.getBatchArrivalId());
                Optional<Product> productOpt = productRepository.findById(itemDTO.getProductId());

                if (batchArrivalOpt.isPresent() && productOpt.isPresent()) {
                    BatchArrival batchArrival = batchArrivalOpt.get();
                    Product product = productOpt.get();

                    BatchArrivalItem batchArrivalItem = getBatchArrivalItem(itemDTO, batchArrival, product);
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
                    return responseMessage.toString();
                }
            }

            if (!batchArrivalItems.isEmpty()) {
                batchArrivalItemRepository.saveAll(batchArrivalItems);
            }

            return responseMessage.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error processing batch arrival items: " + e.getMessage(), e);
        }
    }

    private static BatchArrivalItem getBatchArrivalItem(BatchArrivalItemDTO itemDTO, BatchArrival batchArrival, Product product) {
        BatchArrivalItem batchArrivalItem = new BatchArrivalItem();
        batchArrivalItem.setBatchArrival(batchArrival);
        batchArrivalItem.setProduct(product);
        batchArrivalItem.setQuantityReceived(itemDTO.getQuantityReceived());
        batchArrivalItem.setQuantityRemaining(itemDTO.getQuantityReceived());
        batchArrivalItem.setExpiryDate(itemDTO.getExpiryDate());
        batchArrivalItem.setUnitCost(itemDTO.getUnitCost());
        return batchArrivalItem;
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

    public List<BatchArrivalItem> getByProductBarcode(String barcode) {
        return batchArrivalItemRepository.findByProduct_Barcode(barcode);
    }

    public List<BatchArrivalItem> getByProductBarcodeAndExpiryDate(String barcode, LocalDate expiryDate) {
        return batchArrivalItemRepository.findByProduct_BarcodeAndExpiryDate(barcode, expiryDate);
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
