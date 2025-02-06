package com.example.SmartInventorySystem.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "inventory_batch")
public class InventoryBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long batchId;

    @Column(name = "product_id",nullable = false)
    private Long productID;

    @Column(name = "quantity",nullable = false)
    private Integer quantity;

    @Column(name = "unit_cost",nullable = false)
    private Integer unitCost;

    @Column(name = "expiration_date",nullable = false)
    private Date expirationDate;

    @Column(name = "received_date",nullable = false)
    private Date receivedDate;

    @Column(name = "status",nullable = false)
    private String status;
}
