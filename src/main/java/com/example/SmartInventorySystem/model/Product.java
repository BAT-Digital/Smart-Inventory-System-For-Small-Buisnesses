package com.example.SmartInventorySystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Column(name = "barcode", length = 50, unique = true)
    private String barcode;

    @Column(name = "is_perishable", nullable = false)
    private Boolean isPerishable;

    @Column(name = "is_composite", nullable = false)
    private Boolean isComposite;

    @Column(name = "unit_of_measure", nullable = false, length = 20)
    private String unitOfMeasure;

    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "supplier_id")
    private Supplier supplier;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "volume", nullable = false, precision = 10, scale = 2)
    private BigDecimal volume;
}