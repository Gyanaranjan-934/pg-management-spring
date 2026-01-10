package com.gyan.pg_management.dto.request.booking;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingCancelRequest {
    @NotNull(message = "Booking Id cannot be null")
    private Long bookingId;
}
