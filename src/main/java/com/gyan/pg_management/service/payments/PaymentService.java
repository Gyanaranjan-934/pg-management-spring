package com.gyan.pg_management.service.payments;

import com.gyan.pg_management.dto.request.payment.CreatePaymentRequest;
import com.gyan.pg_management.dto.response.payment.PaymentResponse;
import com.gyan.pg_management.entity.Booking;
import com.gyan.pg_management.entity.Payment;
import com.gyan.pg_management.entity.Tenant;
import com.gyan.pg_management.enums.PaymentMode;

public interface PaymentService {
    PaymentResponse createPayment(CreatePaymentRequest request);
}
