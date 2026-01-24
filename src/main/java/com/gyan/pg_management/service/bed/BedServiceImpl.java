package com.gyan.pg_management.service.bed;

import com.gyan.pg_management.dto.request.bed.BedCreateRequest;
import com.gyan.pg_management.dto.request.bed.BedUpdateRequest;
import com.gyan.pg_management.dto.response.bed.BedResponse;
import com.gyan.pg_management.entity.Bed;
import com.gyan.pg_management.entity.Booking;
import com.gyan.pg_management.entity.Room;
import com.gyan.pg_management.enums.BookingStatus;
import com.gyan.pg_management.mapper.BedMapper;
import com.gyan.pg_management.repository.BedRepository;
import com.gyan.pg_management.repository.BookingRepository;
import com.gyan.pg_management.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BedServiceImpl implements BedService {

    private final BedRepository bedRepository;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    @Transactional
    @Override
    public BedResponse createBed(BedCreateRequest request) {

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(()->new IllegalArgumentException("Room not found with given Id"));

        if (!room.getActive()) {
            throw new IllegalStateException("Cannot add bed to inactive room");
        }

        Long bedCount = bedRepository.countByRoomIdAndBlockedTrue(request.getRoomId());

        if (bedCount >= room.getTotalBeds()) {
            throw new IllegalStateException(
                    "Room is already in full capacity. Please choose a different room."
            );
        }

        Bed bed = Bed.builder()
                .bedNumber(request.getBedNumber())
                .room(room)
                .build();

        Bed savedBed = bedRepository.save(bed);
        log.info("Bed created successfully with ID: {}", savedBed.getId());

        return BedMapper.toResponse(savedBed);
    }

    @Override
    @Transactional
    public BedResponse updateBed(BedUpdateRequest request) {
        Bed bed = bedRepository.findById(request.getBedId())
                .orElseThrow(()->new IllegalArgumentException("Bed not found"));

        bed.setActive(request.getActive());
        bed.setBlocked(request.getBlocked());

        Bed updatedBed = bedRepository.save(bed);

        return BedMapper.toResponse(updatedBed);
    }
}
