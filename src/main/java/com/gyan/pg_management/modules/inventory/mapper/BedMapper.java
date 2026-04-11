package com.gyan.pg_management.modules.inventory.mapper;

import com.gyan.pg_management.modules.inventory.dto.response.BedResponse;
import com.gyan.pg_management.modules.inventory.domain.Bed;

import java.util.Objects;

public final class BedMapper {

    private BedMapper() {
        // prevent instantiation
    }

    public static BedResponse toResponse(Bed bed) {
        Objects.requireNonNull(bed, "Bed entity must not be null for mapping");

        return BedResponse.builder()
                .bedId(bed.getId())
                .bedNumber(bed.getBedNumber())
                .roomNumber(bed.getRoom().getRoomNumber())
                .bedStatus(bed.getStatus())
                .build();
    }
}
