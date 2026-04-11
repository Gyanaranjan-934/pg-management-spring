package com.gyan.pg_management.modules.billing.exception;

import com.gyan.pg_management.shared.exception.BusinessRuntimeException;

public class ExcessPaymentException extends BusinessRuntimeException {
    public ExcessPaymentException(Double amount){
        super("The received payment is more than outstanding amount. Please pay at most amount of "+amount);
    }
}
