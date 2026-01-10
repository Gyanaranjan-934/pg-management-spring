package com.gyan.pg_management.service.booking;

import com.gyan.pg_management.dto.request.booking.BookingCreateRequest;
import com.gyan.pg_management.dto.response.booking.BookingResponse;
import com.gyan.pg_management.entity.Bed;
import com.gyan.pg_management.entity.Booking;
import com.gyan.pg_management.entity.Tenant;

import java.time.LocalDate;

public interface BookingService {
    BookingResponse createBooking(BookingCreateRequest bookingCreateRequest);
    Booking checkoutBooking(Long bookingId, LocalDate checkoutDate);
    Booking cancelBooking(Long bookingId);
}
