package com.example.SmartInventorySystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Column(name = "barcode", nullable = false, length = 50, unique = true)
    private String barcode;

    @Column(name = "is_perishable", nullable = false)
    private Boolean isPerishable;

    @Column(name = "unit_of_measure", nullable = false, length = 20)
    private String unitOfMeasure;

    @Column(name = "supplier", length = 100)
    private String supplier;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}