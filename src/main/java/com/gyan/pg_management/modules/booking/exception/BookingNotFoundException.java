package com.gyan.pg_management.modules.booking.exception;

import com.gyan.pg_management.shared.exception.BusinessRuntimeException;

public class BookingNotFoundException extends BusinessRuntimeException {
    public BookingNotFoundException(Long id) {
        super("Booking not found with ID: " + id);
    }
}
