package com.gyan.pg_management.modules.inventory.service;

import com.gyan.pg_management.modules.inventory.domain.Room;

public interface RoomService {
    Room createRoom(
            Long propertyId,
            String roomNumber,
            Integer floorNumber,
            Integer totalBeds
    );
    void deactivateRoom(Room room);
    Room getRoom(Room room);
}
