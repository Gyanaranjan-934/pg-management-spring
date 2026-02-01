package com.gyan.pg_management.dto.request.booking;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingCancelRequest {
    @NotNull(message = "Booking Id cannot be null")
    private Long bookingId;
}
