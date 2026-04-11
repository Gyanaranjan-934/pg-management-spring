package com.gyan.pg_management.modules.inventory.dto.request;

import com.gyan.pg_management.modules.inventory.domain.BedStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BedUpdateRequest {
    @NotNull(message = "Bed Id is required")
    private Long bedId;

    @NotNull(message = "Bed number cannot be null")
    private String bedNumber;

    @NotNull(message = "Status cannot be null")
    private BedStatus bedStatus;
}
