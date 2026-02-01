package com.gyan.pg_management.mapper;

import com.gyan.pg_management.dto.response.bed.BedResponse;
import com.gyan.pg_management.entity.Bed;

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
