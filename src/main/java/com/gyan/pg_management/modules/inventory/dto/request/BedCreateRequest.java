package com.gyan.pg_management.modules.inventory.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BedCreateRequest {
    @NotNull(message = "Room Id is required")
    private Long roomId;

    @NotNull(message = "Bed number cannot be null")
    private String bedNumber;
}
