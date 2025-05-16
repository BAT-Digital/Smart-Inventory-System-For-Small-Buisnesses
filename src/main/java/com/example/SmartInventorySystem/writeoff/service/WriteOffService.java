package com.example.SmartInventorySystem.writeoff.service;


import com.example.SmartInventorySystem.batcharrivalitem.entity.BatchArrivalItem;
import com.example.SmartInventorySystem.batcharrivalitem.repository.BatchArrivalItemRepository;
import com.example.SmartInventorySystem.writeoff.dto.WriteOffDTO;
import com.example.SmartInventorySystem.writeoff.entity.WriteOff;
import com.example.SmartInventorySystem.writeoff.repository.WriteOffRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WriteOffService {

    private final WriteOffRepository writeOffRepository;

    private final BatchArrivalItemRepository batchArrivalItemRepository;

    public WriteOffService(WriteOffRepository writeOffRepository,BatchArrivalItemRepository batchArrivalItemRepository) {
        this.writeOffRepository = writeOffRepository;
        this.batchArrivalItemRepository = batchArrivalItemRepository;

    }

    public List<WriteOff> getAllWriteOffs() {
        return writeOffRepository.findAll();
    }

    public List<WriteOff> searchAllWriteOffs(String searchTerm) {
        return writeOffRepository.search(
                searchTerm.toLowerCase() + "%");
    }

    public Optional<WriteOff> getWriteOffById(Long id) {
        return writeOffRepository.findById(id);
    }

    public WriteOff createWriteOff(WriteOff writeOff) {
        return writeOffRepository.save(writeOff);
    }

    @Transactional
    public String createWriteOffByDTO(WriteOffDTO writeOffDto) {
        Optional<BatchArrivalItem> batchArrivalItemOpt =
                batchArrivalItemRepository.findById(writeOffDto.getBatchItemId());

        if (batchArrivalItemOpt.isPresent()) {
            BatchArrivalItem batchArrivalItem = batchArrivalItemOpt.get();

            WriteOff writeOff = new WriteOff();
            writeOff.setBatch(batchArrivalItem);
            writeOff.setQuantity(writeOffDto.getQuantity());
            writeOff.setReason(writeOffDto.getReason());

            batchArrivalItem.setQuantityRemaining(batchArrivalItem.getQuantityRemaining().subtract(writeOffDto.getQuantity()));

            writeOffRepository.save(writeOff);
        } else {
            return "couldn't find batchArrivalItem";
        }
        return "created WriteOff";
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
