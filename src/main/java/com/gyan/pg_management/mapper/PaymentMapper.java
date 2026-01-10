package com.gyan.pg_management.mapper;

import com.gyan.pg_management.dto.response.payment.PaymentResponse;
import com.gyan.pg_management.entity.Payment;

public final class PaymentMapper {

    private PaymentMapper() {}

    public static PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .amount(payment.getAmount())
                .paymentMode(payment.getPaymentMode())
                .paymentDate(payment.getCreatedAt())
                .build();
    }
}

