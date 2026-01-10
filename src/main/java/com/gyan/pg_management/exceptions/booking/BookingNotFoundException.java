package com.gyan.pg_management.exceptions.booking;

import com.gyan.pg_management.exceptions.BusinessRuntimeException;

public class BookingNotFoundException extends BusinessRuntimeException {
    public BookingNotFoundException(Long id) {
        super("Booking not found with ID: " + id);
    }
}
