package com.example.SmartInventorySystem.salestransaction.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sales_transactions")
public class SalesTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "credentials")
    private String credentials;

    @Column(name = "status")
    private String status;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;
}
