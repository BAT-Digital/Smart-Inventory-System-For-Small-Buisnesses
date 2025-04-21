package com.example.SmartInventorySystem.productinuse.service;

import com.example.SmartInventorySystem.batcharrivalitem.entity.BatchArrivalItem;
import com.example.SmartInventorySystem.batcharrivalitem.repository.BatchArrivalItemRepository;
import com.example.SmartInventorySystem.productinuse.entity.ProductInUse;
import com.example.SmartInventorySystem.productinuse.repository.ProductInUseRepository;
import com.example.SmartInventorySystem.shared.dto.ProductRequestDTO;

import com.example.SmartInventorySystem.product.entity.Product;
import com.example.SmartInventorySystem.product.repository.ProductRepository;


import com.example.SmartInventorySystem.user.entity.User;
import com.example.SmartInventorySystem.user.repository.UserRepository;
import com.example.SmartInventorySystem.writeoff.entity.WriteOff;
import com.example.SmartInventorySystem.writeoff.repository.WriteOffRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductInUseService {

    private final ProductInUseRepository productInUseRepository;
    private final BatchArrivalItemRepository batchArrivalItemRepository;
    private final WriteOffRepository writeOffRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductInUseService(ProductInUseRepository productInUseRepository,
                               ProductRepository productRepository,
                               BatchArrivalItemRepository batchArrivalItemRepository,
                               WriteOffRepository writeOffRepository,
                               UserRepository userRepository) {
        this.productInUseRepository = productInUseRepository;
        this.productRepository = productRepository;
        this.batchArrivalItemRepository = batchArrivalItemRepository;
        this.writeOffRepository = writeOffRepository;
        this.userRepository = userRepository;
    }



    @Transactional
    public String moveToProductInUse(List<ProductRequestDTO> productRequestDTOS) {
        StringBuilder responseMessage = new StringBuilder();

        for (ProductRequestDTO productDTO : productRequestDTOS) {
            Optional<Product> existingProduct = productRepository.findByBarcode(productDTO.getBarcode());

            if (existingProduct.isEmpty()) {
                return "Product not found for barcode: " + productDTO.getBarcode();
            }

            Product product = existingProduct.get();

            Optional<BatchArrivalItem> batchItemOpt = batchArrivalItemRepository.findByProductAndExpiryDate(
                    product, productDTO.getExpirationDate()
            );

            if (batchItemOpt.isEmpty()) {
                return "No stock available in main inventory for: " + product.getProductName();
            }

            BatchArrivalItem batchItem = batchItemOpt.get();

            if (batchItem.getQuantityRemaining().compareTo(productDTO.getQuantity()) < 0) {
                return "Not enough stock in main inventory for: " + product.getProductName();
            }

            Optional<User> userOpt = userRepository.findById(productDTO.getUserId());

            if (userOpt.isEmpty()) {
                return "User not found by id: " + productDTO.getUserId();
            }

            User user = userOpt.get();


            // Find or create ProductInUse entry
            Optional<ProductInUse> optionalProductInUse = productInUseRepository.findByProduct(product);

            BigDecimal newVolumeReceived = BigDecimal.valueOf(0);

            ProductInUse productInUse;
            if (optionalProductInUse.isPresent()) {
                productInUse = optionalProductInUse.get();

                newVolumeReceived = productInUse.getVolumeRemaining().add(productDTO.getQuantity().multiply(product.getVolume()));

                // Log the deassigned productInUse
                WriteOff writeOff = new WriteOff();
                writeOff.setBatch(batchItem);
                writeOff.setQuantity(productInUse.getVolumeReceived().subtract(productInUse.getVolumeRemaining().multiply(product.getVolume())));
                writeOff.setReason("De-assigned from Product In Use");
                writeOffRepository.save(writeOff);
            } else {
                productInUse = new ProductInUse();
                newVolumeReceived = productDTO.getQuantity().multiply(product.getVolume());
            }

            productInUse.setProduct(product);
            productInUse.setVolumeReceived(newVolumeReceived);
            productInUse.setVolumeRemaining(newVolumeReceived);
            productInUse.setAssignedBy(user);


            // Subtract quantity from BatchArrivalItem (Main Inventory)
            batchItem.setQuantityRemaining(batchItem.getQuantityRemaining().subtract(productDTO.getQuantity()));


            // Save updates
            productInUseRepository.save(productInUse);
            batchArrivalItemRepository.save(batchItem);

            responseMessage.append("Added ").append(productDTO.getQuantity()).append(" of ").append(product.getProductName()).append(" to active use, by ").append(user.getUsername());
        }

        return responseMessage.toString();
    }






    public List<ProductInUse> getAllProductsInUse() {
        return productInUseRepository.findAll();
    }

    public Optional<ProductInUse> getProductInUseById(Long id) {
        return productInUseRepository.findById(id);
    }

    public ProductInUse createProductInUse(ProductInUse productInUse) {
        return productInUseRepository.save(productInUse);
    }

    public ProductInUse updateProductInUse(Long id, ProductInUse updatedProductInUse) {
        return productInUseRepository.findById(id).map(productInUse -> {
            productInUse.setVolumeReceived(updatedProductInUse.getVolumeReceived());
            productInUse.setVolumeRemaining(updatedProductInUse.getVolumeRemaining());
            return productInUseRepository.save(productInUse);
        }).orElse(null);
    }

    public void deleteProductInUse(Long id) {
        productInUseRepository.deleteById(id);
    }
}
