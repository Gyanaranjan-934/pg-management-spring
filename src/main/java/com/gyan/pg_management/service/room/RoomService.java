package com.gyan.pg_management.service.room;

import com.gyan.pg_management.entity.Room;

public interface RoomService {
    Room createRoom(
            Long propertyId,
            String roomNumber,
            Integer floorNumber,
            Integer totalBeds
    );
    void deactivateRoom(Long roomId);
}
