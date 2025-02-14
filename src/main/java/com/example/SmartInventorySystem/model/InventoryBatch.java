package com.example.SmartInventorySystem.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inventory_batch")
public class InventoryBatch {

    @Id
    @Column(name = "batch_id")
    private Long batchId;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_cost", nullable = false, precision = 10)
    private BigDecimal unitCost;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @CreationTimestamp
    @Column(name = "received_date", updatable = false)
    private LocalDateTime receivedDate;

    @Column(name = "status")
    private String status = "active";
}