package com.gyan.pg_management.shared.exception;

import com.gyan.pg_management.shared.exception.BusinessRuntimeException;

public class UserNotFoundException extends BusinessRuntimeException {
    public UserNotFoundException(String message){
        super(message);
    }
}
