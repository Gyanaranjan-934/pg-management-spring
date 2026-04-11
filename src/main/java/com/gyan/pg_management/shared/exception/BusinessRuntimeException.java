package com.gyan.pg_management.shared.exception;

public class BusinessRuntimeException extends RuntimeException{
    public BusinessRuntimeException(String message){
        super(message);
    }
}
