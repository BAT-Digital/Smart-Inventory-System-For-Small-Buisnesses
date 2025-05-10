package com.example.SmartInventorySystem.writeoff.service;


import com.example.SmartInventorySystem.writeoff.entity.WriteOff;
import com.example.SmartInventorySystem.writeoff.repository.WriteOffRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WriteOffService {

    private final WriteOffRepository writeOffRepository;

    public WriteOffService(WriteOffRepository writeOffRepository) {
        this.writeOffRepository = writeOffRepository;
    }

    public List<WriteOff> getAllWriteOffs() {
        return writeOffRepository.findAll();
    }

    public Optional<WriteOff> getWriteOffById(Long id) {
        return writeOffRepository.findById(id);
    }

    public WriteOff createWriteOff(WriteOff writeOff) {
        return writeOffRepository.save(writeOff);
    }

    public WriteOff updateWriteOff(Long id, WriteOff updatedWriteOff) {
        return writeOffRepository.findById(id).map(writeOff -> {
            writeOff.setBatch(updatedWriteOff.getBatch());
            writeOff.setQuantity(updatedWriteOff.getQuantity());
            writeOff.setReason(updatedWriteOff.getReason());
            return writeOffRepository.save(writeOff);
        }).orElse(null);
    }

    public void deleteWriteOff(Long id) {
        writeOffRepository.deleteById(id);
    }
}
