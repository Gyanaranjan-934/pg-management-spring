package com.gyan.pg_management.dto.request.booking;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BookingCheckoutRequest {
    @NotNull(message = "Booking Id cannot be null")
    private Long bookingId;
    @NotNull(message = "Checkout Date must not be null")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate checkoutDate;
}
