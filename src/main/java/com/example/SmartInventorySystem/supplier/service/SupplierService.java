package com.example.SmartInventorySystem.supplier.service;


import com.example.SmartInventorySystem.supplier.dto.SupplierDTO;
import com.example.SmartInventorySystem.supplier.entity.Supplier;
import com.example.SmartInventorySystem.supplier.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    public Supplier createSupplierByDto(SupplierDTO supplierDTO) {
        Supplier supplier = new Supplier();
        supplier.setName(supplierDTO.getName());
        supplier.setAddress(supplierDTO.getAddress());
        supplier.setContactInfo(supplierDTO.getContactInfo());
        return supplierRepository.save(supplier);
    }

    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long id, Supplier updatedSupplier) {
        return supplierRepository.findById(id).map(supplier -> {
            supplier.setName(updatedSupplier.getName());
            supplier.setAddress(updatedSupplier.getAddress());
            supplier.setContactInfo(updatedSupplier.getContactInfo());
            return supplierRepository.save(supplier);
        }).orElse(null);
    }

    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
}
