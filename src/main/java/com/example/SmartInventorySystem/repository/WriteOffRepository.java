package com.example.SmartInventorySystem.repository;

import com.example.SmartInventorySystem.model.WriteOff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WriteOffRepository extends JpaRepository<WriteOff, Long> {
}
