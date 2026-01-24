package com.gyan.pg_management.service.bed;

import com.gyan.pg_management.dto.request.bed.BedCreateRequest;
import com.gyan.pg_management.dto.request.bed.BedUpdateRequest;
import com.gyan.pg_management.dto.response.bed.BedResponse;
import com.gyan.pg_management.entity.Bed;
import com.gyan.pg_management.entity.Room;

public interface BedService {
    BedResponse createBed(BedCreateRequest request);
    BedResponse updateBed(BedUpdateRequest request);
}
