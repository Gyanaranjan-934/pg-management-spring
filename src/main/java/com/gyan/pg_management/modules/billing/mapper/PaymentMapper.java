package com.gyan.pg_management.modules.billing.mapper;

import com.gyan.pg_management.modules.billing.dto.response.PaymentResponse;
import com.gyan.pg_management.modules.billing.domain.Payment;

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

