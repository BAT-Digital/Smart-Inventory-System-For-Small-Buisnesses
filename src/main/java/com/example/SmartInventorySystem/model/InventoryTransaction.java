package com.example.SmartInventorySystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inventory_transaction")
public class InventoryTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(name = "transaction_type",nullable = false, length = 50)
    private String transactionType;  // e.g., "sale", "write-off", "incoming"

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();

    // Optionally, reference the User if you want to track who performed the transaction.
    @Column(name = "user_id")
    private int userID;  // Make sure the User entity exists if you choose to include this.

    @Column(name = "total_amount",precision = 10, scale = 2)
    private Double totalAmount;  // Optional: total amount of the transaction

    // Constructors, Getters, and Setters
    public InventoryTransaction() {
    }
}