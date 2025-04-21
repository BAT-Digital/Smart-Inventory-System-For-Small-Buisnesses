package com.example.SmartInventorySystem.supplier.controller;

import com.example.SmartInventorySystem.supplier.dto.SupplierDTO;
import com.example.SmartInventorySystem.supplier.entity.Supplier;
import com.example.SmartInventorySystem.supplier.service.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable Long id) {
        Optional<Supplier> supplier = supplierService.getSupplierById(id);
        return supplier.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create-by-dto")
    public ResponseEntity<Supplier> createSupplierByDto(@RequestBody SupplierDTO supplierDTO) {
        Supplier createdSupplier = supplierService.createSupplierByDto(supplierDTO);
        return new ResponseEntity<>(createdSupplier, HttpStatus.CREATED);
    }

    @PostMapping("/create-by-entity")
    public Supplier createSupplier(@RequestBody Supplier supplier) {
        return supplierService.createSupplier(supplier);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id, @RequestBody Supplier supplier) {
        Supplier updatedSupplier = supplierService.updateSupplier(id, supplier);
        return updatedSupplier != null ? ResponseEntity.ok(updatedSupplier) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
