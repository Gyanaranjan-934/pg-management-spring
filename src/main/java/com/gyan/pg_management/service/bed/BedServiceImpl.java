package com.gyan.pg_management.service.bed;

import com.gyan.pg_management.entity.Bed;
import com.gyan.pg_management.entity.Booking;
import com.gyan.pg_management.entity.Room;
import com.gyan.pg_management.enums.BookingStatus;
import com.gyan.pg_management.repository.BedRepository;
import com.gyan.pg_management.repository.BookingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BedServiceImpl implements BedService {

    private final BedRepository bedRepository;
    private final BookingRepository bookingRepository;

    public Bed createBed(String bedNumber, Room room) {
        if (!room.getActive()) {
            throw new IllegalStateException("Cannot add bed to inactive room");
        }
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
    @Override
    public void blockBed(Long bedId) {
        Bed toUpdateBed = getBed(bedId);
        toUpdateBed.setBlocked(true);
        bedRepository.save(toUpdateBed);
    }
    @Override
    public void unblockBed(Long bedId) {
        Bed toUpdateBed = getBed(bedId);
        toUpdateBed.setBlocked(false);
        bedRepository.save(toUpdateBed);
    }

    @Override
    public void deactivateBed(Long bedId) {
        // 1. Find the bed or fail early
        Bed foundBed = getBed(bedId);
        // 2. Check for active bookings
        boolean hasActiveBooking = bookingRepository.findByBedAndStatus(foundBed, BookingStatus.ACTIVE).isPresent();

        if (hasActiveBooking) {
            throw new IllegalStateException("Cannot deactivate bed with active booking");
        }

        // 3. Perform update
        foundBed.setActive(false);
        foundBed.setBlocked(false);
        bedRepository.save(foundBed);
    }

    @Override
    public Bed getBed(Long bedId){
        return bedRepository.findById(bedId)
                .orElseThrow(() -> new IllegalStateException("Bed not found!!!"));
    }
}
