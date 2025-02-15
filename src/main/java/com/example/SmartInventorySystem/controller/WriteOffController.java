package com.example.SmartInventorySystem.controller;

import com.example.SmartInventorySystem.model.WriteOff;
import com.example.SmartInventorySystem.service.WriteOffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/write-offs")
public class WriteOffController {

    private final WriteOffService writeOffService;

    public WriteOffController(WriteOffService writeOffService) {
        this.writeOffService = writeOffService;
    }

    @GetMapping
    public List<WriteOff> getAllWriteOffs() {
        return writeOffService.getAllWriteOffs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WriteOff> getWriteOffById(@PathVariable Long id) {
        Optional<WriteOff> writeOff = writeOffService.getWriteOffById(id);
        return writeOff.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public WriteOff createWriteOff(@RequestBody WriteOff writeOff) {
        return writeOffService.createWriteOff(writeOff);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WriteOff> updateWriteOff(@PathVariable Long id, @RequestBody WriteOff writeOff) {
        WriteOff updatedWriteOff = writeOffService.updateWriteOff(id, writeOff);
        return updatedWriteOff != null ? ResponseEntity.ok(updatedWriteOff) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWriteOff(@PathVariable Long id) {
        writeOffService.deleteWriteOff(id);
        return ResponseEntity.noContent().build();
    }
}
