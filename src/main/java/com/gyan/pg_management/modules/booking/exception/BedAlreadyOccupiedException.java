package com.gyan.pg_management.modules.booking.exception;

import com.gyan.pg_management.shared.exception.BusinessRuntimeException;

public class BedAlreadyOccupiedException extends BusinessRuntimeException {
    public BedAlreadyOccupiedException(Long id) {
        super("Booking not found with ID: " + id);
    }
}
