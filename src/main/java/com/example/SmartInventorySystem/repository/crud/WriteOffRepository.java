package com.example.SmartInventorySystem.repository.crud;

import com.example.SmartInventorySystem.model.WriteOff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WriteOffRepository extends JpaRepository<WriteOff, Long> {
}
