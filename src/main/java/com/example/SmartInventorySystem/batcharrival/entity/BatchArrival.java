package com.example.SmartInventorySystem.batcharrival.entity;


import com.example.SmartInventorySystem.supplier.entity.Supplier;
import com.example.SmartInventorySystem.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "batch_arrivals")
public class BatchArrival {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "arrival_id")
    private Long arrivalId;

    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "supplier_id")
    private Supplier supplier;

    @Column(name = "arrival_date", nullable = false)
    private LocalDateTime arrivalDate = LocalDateTime.now();

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "added_by", referencedColumnName = "user_id", nullable = false)
    private User addedBy;
}
