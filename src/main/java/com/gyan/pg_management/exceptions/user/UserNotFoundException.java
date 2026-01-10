package com.gyan.pg_management.exceptions.user;

import com.gyan.pg_management.exceptions.BusinessRuntimeException;

public class UserNotFoundException extends BusinessRuntimeException {
    public UserNotFoundException(String message){
        super(message);
    }
}
