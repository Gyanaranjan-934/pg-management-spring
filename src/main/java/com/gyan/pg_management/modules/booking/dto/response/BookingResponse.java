package com.gyan.pg_management.modules.booking.dto.response;

import com.gyan.pg_management.modules.booking.domain.BookingStatus;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class BookingResponse {
    Long bookingId;
    BookingStatus status;
    LocalDate startDate;
    LocalDate endDate;
}
