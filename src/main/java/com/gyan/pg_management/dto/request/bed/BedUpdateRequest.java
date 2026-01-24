package com.gyan.pg_management.dto.request.bed;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BedUpdateRequest {
    @NotNull(message = "Bed Id is required")
    private Long bedId;

    @NotNull(message = "Bed number cannot be null")
    private String bedNumber;

    @NotNull(message = "Active status cannot be null")
    private Boolean active;

    @NotNull(message = "Block status cannot be null")
    private Boolean blocked;
}
