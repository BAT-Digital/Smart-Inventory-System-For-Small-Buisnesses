package com.example.SmartInventorySystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(name = "username",nullable = false,length = 100)
    private String username;

    @Column(name = "password_hash",nullable = false,length = 255)
    private String passwordHash;

    @Column(name = "role",nullable = false,length = 50)
    private String role;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();;
}
