package com.gyan.pg_management.mapper;

import com.gyan.pg_management.dto.response.booking.BookingResponse;
import com.gyan.pg_management.entity.Booking;

import java.util.Objects;

public final class BookingMapper {

    private BookingMapper() {
        // prevent instantiation
    }

    public static BookingResponse toResponse(Booking booking) {
        Objects.requireNonNull(booking, "Booking entity must not be null for mapping");

        return BookingResponse.builder()
                .bookingId(booking.getId())
                .status(booking.getStatus())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();
    }
}
