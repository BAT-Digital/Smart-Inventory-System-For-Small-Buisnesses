package com.example.SmartInventorySystem.productinuse.entity;

import com.example.SmartInventorySystem.product.entity.Product;
import com.example.SmartInventorySystem.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "products_in_use")
public class ProductInUse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_in_use_id")
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "assigned_by", referencedColumnName = "user_id", nullable = false)
    private User assignedBy;

    @Column(name = "assigned_date", nullable = false, updatable = false)
    private LocalDateTime assignedDate = LocalDateTime.now();

    @Column(name = "volume_received", nullable = false, precision = 10, scale = 2)
    private BigDecimal volumeReceived;

    @Column(name = "volume_remaining", nullable = false, precision = 10, scale = 2)
    private BigDecimal volumeRemaining;
}