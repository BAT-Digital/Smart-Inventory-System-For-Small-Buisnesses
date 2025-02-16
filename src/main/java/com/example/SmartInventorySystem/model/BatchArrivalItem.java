package com.example.SmartInventorySystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "batch_arrival_items")
public class BatchArrivalItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_item_id")
    private Long batchItemId;

    @ManyToOne
    @JoinColumn(name = "arrival_id", referencedColumnName = "arrival_id", nullable = false)
    private BatchArrival batchArrival;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity_received", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantityReceived;

    @Column(name = "quantity_remaining", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantityRemaining;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "received_date", nullable = false, updatable = false)
    private LocalDate receivedDate = LocalDate.now();

    @Column(name = "unit_cost", precision = 10, scale = 2)
    private BigDecimal unitCost;

    @ManyToOne
    @JoinColumn(name = "added_by", referencedColumnName = "user_id", nullable = false)
    private User addedBy;
}