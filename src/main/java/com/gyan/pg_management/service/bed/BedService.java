package com.gyan.pg_management.service.bed;

import com.gyan.pg_management.entity.Bed;
import com.gyan.pg_management.entity.Room;

public interface BedService {
    Bed createBed(String bedNumber, Room room);
    void deactivateBed(Long bedId);
    void blockBed(Long bedId);
    void unblockBed(Long bedId);
    Bed getBed(Long bedId);
}
