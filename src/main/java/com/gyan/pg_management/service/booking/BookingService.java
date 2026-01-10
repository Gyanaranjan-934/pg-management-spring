package com.gyan.pg_management.service.booking;

import com.gyan.pg_management.dto.request.booking.BookingCancelRequest;
import com.gyan.pg_management.dto.request.booking.BookingCheckoutRequest;
import com.gyan.pg_management.dto.request.booking.BookingCreateRequest;
import com.gyan.pg_management.dto.response.booking.BookingResponse;


public interface BookingService {
    BookingResponse createBooking(BookingCreateRequest bookingCreateRequest);
    BookingResponse checkoutBooking(BookingCheckoutRequest bookingCheckoutRequest);
    BookingResponse cancelBooking(BookingCancelRequest bookingCancelRequest);
}
