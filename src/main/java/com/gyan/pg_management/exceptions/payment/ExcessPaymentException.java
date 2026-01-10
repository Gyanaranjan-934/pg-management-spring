package com.gyan.pg_management.exceptions.payment;

import com.gyan.pg_management.exceptions.BusinessRuntimeException;

public class ExcessPaymentException extends BusinessRuntimeException {
    public ExcessPaymentException(Double amount){
        super("The received payment is more than outstanding amount. Please pay at most amount of "+amount);
    }
}
