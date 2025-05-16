package com.example.SmartInventorySystem.writeoff.controller;


import com.example.SmartInventorySystem.product.entity.Product;
import com.example.SmartInventorySystem.writeoff.dto.WriteOffDTO;
import com.example.SmartInventorySystem.writeoff.entity.WriteOff;
import com.example.SmartInventorySystem.writeoff.service.WriteOffService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public List<WriteOff> getAllWriteOffs(
            @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return writeOffService.searchAllWriteOffs(search);
        }
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

    @PostMapping("/by-dto")
    public String createWriteOffByDTO(@RequestBody WriteOffDTO writeOffDto) {
        return writeOffService.createWriteOffByDTO(writeOffDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WriteOff> updateWriteOff(@PathVariable Long id, @RequestBody WriteOff writeOff) {
        WriteOff updatedWriteOff = writeOffService.updateWriteOff(id, writeOff);
        return updatedWriteOff != null ? ResponseEntity.ok(updatedWriteOff) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWriteOff(@PathVariable Long id) {
        writeOffService.deleteWriteOff(id);
        return ResponseEntity.noContent().build();
    }
}
