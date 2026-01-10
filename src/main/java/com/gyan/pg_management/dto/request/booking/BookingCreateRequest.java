package com.gyan.pg_management.dto.request.booking;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class BookingCreateRequest {

    @NotNull(message = "Tenant ID is required")
    private Long tenantId;

    @NotNull(message = "Bed ID is required")
    private Long bedId;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "Monthly rent is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Monthly rent must be greater than zero")
    private Double monthlyRent;

    @NotNull(message = "Security deposit is required")
    @PositiveOrZero(message = "Security deposit cannot be negative")
    private Double securityDeposit;
}
