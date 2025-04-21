package com.example.SmartInventorySystem.supplier.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "contact_info", length = 255)
    private String contactInfo;
}