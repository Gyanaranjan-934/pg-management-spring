package com.gyan.pg_management.service.payments;

import com.gyan.pg_management.entity.Booking;
import com.gyan.pg_management.entity.Payment;
import com.gyan.pg_management.entity.Tenant;
import com.gyan.pg_management.enums.PaymentMode;

public interface PaymentService {
    Payment recordPayment(
            Booking booking,
            Tenant tenant,
            Double amount,
            PaymentMode paymentMode,
            String remarks
    );
}
