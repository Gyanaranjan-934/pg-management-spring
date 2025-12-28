package com.gyan.pg_management.service.bed;

import com.gyan.pg_management.entity.Bed;
import com.gyan.pg_management.entity.Room;
import com.gyan.pg_management.repository.BedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BedServiceImpl implements BedService {

    private final BedRepository bedRepository;

    public Bed createBed(String bedNumber, Room room) {

        Long bedCount = bedRepository.getTotalBedCountOfRoom(room.getId());
        long currentBedCount = bedCount == null ? 0 : bedCount;

        if (currentBedCount >= room.getTotalBeds()) {
            throw new IllegalStateException(
                    "Room is already in full capacity. Please choose a different room."
            );
        }

        Bed bed = Bed.builder()
                .bedNumber(bedNumber)
                .room(room)
                .build();

        return bedRepository.save(bed);
    }
}
