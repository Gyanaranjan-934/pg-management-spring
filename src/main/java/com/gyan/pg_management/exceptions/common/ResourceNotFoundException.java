package com.gyan.pg_management.exceptions.common;

import com.gyan.pg_management.exceptions.BusinessRuntimeException;

public class ResourceNotFoundException extends BusinessRuntimeException {
    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " not found with ID: " + id);
    }
}
