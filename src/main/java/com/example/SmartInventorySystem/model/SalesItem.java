package com.example.SmartInventorySystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "sales_items")
public class SalesItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sales_item_id")
    private Long salesItemId;

    @ManyToOne
    @JoinColumn(name = "transaction_id", referencedColumnName = "transaction_id", nullable = false)
    private SalesTransaction salesTransaction;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;
}
