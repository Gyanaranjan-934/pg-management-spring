package com.gyan.pg_management.service.payments;

import com.gyan.pg_management.dto.request.payment.CreatePaymentRequest;
import com.gyan.pg_management.dto.response.payment.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPayment(CreatePaymentRequest request);
}
