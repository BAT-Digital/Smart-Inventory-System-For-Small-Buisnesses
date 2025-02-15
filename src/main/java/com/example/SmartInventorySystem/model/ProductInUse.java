package com.example.SmartInventorySystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "products_in_use")
public class ProductInUse {

    @Id
    @Column(name = "product_id")
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "assigned_by", referencedColumnName = "user_id", nullable = false)
    private User assignedBy;

    @Column(name = "assigned_date", nullable = false, updatable = false)
    private LocalDateTime assignedDate = LocalDateTime.now();

    @Column(name = "deassigned_date")
    private LocalDateTime deassignedDate;

    @Column(name = "volume", nullable = false, precision = 10, scale = 2)
    private BigDecimal volume;
}