package com.gyan.pg_management.dto.request.bed;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BedCreateRequest {
    @NotNull(message = "Room Id is required")
    private Long roomId;

    @NotNull(message = "Bed number cannot be null")
    private String bedNumber;
}
