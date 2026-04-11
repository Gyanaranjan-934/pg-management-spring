package com.gyan.pg_management.modules.inventory.service;

import com.gyan.pg_management.modules.inventory.dto.request.BedCreateRequest;
import com.gyan.pg_management.modules.inventory.dto.request.BedUpdateRequest;
import com.gyan.pg_management.modules.inventory.dto.response.BedResponse;
import com.gyan.pg_management.modules.inventory.domain.Bed;
import com.gyan.pg_management.modules.inventory.domain.Room;
import com.gyan.pg_management.modules.inventory.domain.BedStatus;
import com.gyan.pg_management.modules.inventory.mapper.BedMapper;
import com.gyan.pg_management.modules.inventory.repository.BedRepository;
import com.gyan.pg_management.modules.booking.repository.BookingRepository;
import com.gyan.pg_management.modules.inventory.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

        Long bedCount = bedRepository.countByIdAndStatusIn(request.getRoomId(),
                                            List.of(BedStatus.OCCUPIED, BedStatus.RESERVED));

        if (bedCount >= room.getTotalBeds()) {
            throw new IllegalStateException(
                    "Room is already in full capacity. Please choose a different room."
            );
        }

        Bed bed = Bed.builder()
                .bedNumber(request.getBedNumber())
                .room(room)
                .status(BedStatus.VACANT)
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

        bed.setStatus(request.getBedStatus());
        Bed updatedBed = bedRepository.save(bed);

        return BedMapper.toResponse(updatedBed);
    }
}
