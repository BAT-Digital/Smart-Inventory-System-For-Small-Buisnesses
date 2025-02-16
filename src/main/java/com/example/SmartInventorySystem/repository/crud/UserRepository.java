package com.example.SmartInventorySystem.repository.crud;

import com.example.SmartInventorySystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
