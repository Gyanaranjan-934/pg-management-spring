package com.gyan.pg_management.exceptions.booking;

import com.gyan.pg_management.exceptions.BusinessRuntimeException;

public class BedAlreadyOccupiedException extends BusinessRuntimeException {
    public BedAlreadyOccupiedException(Long id) {
        super("Booking not found with ID: " + id);
    }
}
