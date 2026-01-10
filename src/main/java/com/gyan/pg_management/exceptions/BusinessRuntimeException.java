package com.gyan.pg_management.exceptions;

public class BusinessRuntimeException extends RuntimeException{
    public BusinessRuntimeException(String message){
        super(message);
    }
}
