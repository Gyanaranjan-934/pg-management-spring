package com.gyan.pg_management.dto.response.booking;

import com.gyan.pg_management.enums.BookingStatus;
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
