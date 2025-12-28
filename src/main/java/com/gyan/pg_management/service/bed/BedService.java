package com.gyan.pg_management.service.bed;

import com.gyan.pg_management.entity.Bed;
import com.gyan.pg_management.entity.Room;

public interface BedService {
    Bed createBed(String bedNumber, Room room);
}
