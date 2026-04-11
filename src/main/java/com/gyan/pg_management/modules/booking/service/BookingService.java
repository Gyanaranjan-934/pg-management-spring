package com.gyan.pg_management.modules.booking.service;

import com.gyan.pg_management.modules.booking.dto.request.BookingCancelRequest;
import com.gyan.pg_management.modules.booking.dto.request.BookingCheckoutRequest;
import com.gyan.pg_management.modules.booking.dto.request.BookingCreateRequest;
import com.gyan.pg_management.modules.booking.dto.response.BookingResponse;


public interface BookingService {
    BookingResponse createBooking(BookingCreateRequest bookingCreateRequest);
    BookingResponse checkoutBooking(BookingCheckoutRequest bookingCheckoutRequest);
    BookingResponse cancelBooking(BookingCancelRequest bookingCancelRequest);
}
