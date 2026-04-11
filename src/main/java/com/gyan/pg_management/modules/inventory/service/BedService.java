package com.gyan.pg_management.modules.inventory.service;

import com.gyan.pg_management.modules.inventory.dto.request.BedCreateRequest;
import com.gyan.pg_management.modules.inventory.dto.request.BedUpdateRequest;
import com.gyan.pg_management.modules.inventory.dto.response.BedResponse;
import com.gyan.pg_management.modules.inventory.domain.Bed;
import com.gyan.pg_management.modules.inventory.domain.Room;

public interface BedService {
    BedResponse createBed(BedCreateRequest request);
    BedResponse updateBed(BedUpdateRequest request);
}
