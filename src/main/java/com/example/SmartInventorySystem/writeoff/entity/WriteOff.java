package com.example.SmartInventorySystem.writeoff.entity;

import com.example.SmartInventorySystem.batcharrivalitem.entity.BatchArrivalItem;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "write_off")
public class WriteOff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "write_off_id")
    private Long writeOffId;

    @ManyToOne
    @JoinColumn(name = "batch_item_id", referencedColumnName = "batch_item_id", nullable = false)
    private BatchArrivalItem batch;

    @Column(name = "write_off_date", nullable = false, updatable = false)
    private LocalDate writeOffDate = LocalDate.now();

    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;
}