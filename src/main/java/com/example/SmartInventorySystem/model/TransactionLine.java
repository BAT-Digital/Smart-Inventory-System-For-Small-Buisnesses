package com.example.SmartInventorySystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "transaction_line")
public class TransactionLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private InventoryTransaction inventoryTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    private InventoryBatch inventoryBatch;

    @Column(nullable = false)
    private Integer quantityChange;  // Negative for deductions (e.g., sales), positive for additions

    @Column(precision = 10, scale = 2)
    private Double unitCost;  // Capture the cost at the time of transaction, if needed

    // Constructors, Getters, and Setters
    public TransactionLine() {
    }

    // Getters and setters...
}