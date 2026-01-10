package com.gyan.pg_management.service.room;

import com.gyan.pg_management.entity.Property;
import com.gyan.pg_management.entity.Room;
import com.gyan.pg_management.repository.PropertyRepository;
import com.gyan.pg_management.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{

    private final RoomRepository roomRepository;
    private final PropertyRepository propertyRepository;

    @Override
    public Room createRoom(
            Long propertyId,
            String roomNumber,
            Integer floorNumber,
            Integer totalBeds
    ) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found"));

        Room room = Room.builder()
                .roomNumber(roomNumber)
                .floorNumber(floorNumber)
                .totalBeds(totalBeds)
                .property(property)
                .active(true)
                .build();

        return roomRepository.save(room);
    }


    @Override
    public void deactivateRoom(Room room) {
        Room toUpdateRoom = getRoom(room);

        toUpdateRoom.setActive(false);
        roomRepository.save(toUpdateRoom);
    }


    @Override
    public Room getRoom(Room room){
        return roomRepository.findById(room.getId())
                .orElseThrow(()->new IllegalStateException("Room doesn't exists"));
    }
}
