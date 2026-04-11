package com.gyan.pg_management.modules.billing.service;

import com.gyan.pg_management.modules.billing.dto.request.CreatePaymentRequest;
import com.gyan.pg_management.modules.billing.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPayment(CreatePaymentRequest request);
}
