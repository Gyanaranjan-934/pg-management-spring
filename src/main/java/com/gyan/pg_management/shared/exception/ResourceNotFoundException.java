package com.gyan.pg_management.shared.exception;

import com.gyan.pg_management.shared.exception.BusinessRuntimeException;

public class ResourceNotFoundException extends BusinessRuntimeException {
    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " not found with ID: " + id);
    }
}
