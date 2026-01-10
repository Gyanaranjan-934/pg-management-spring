package com.gyan.pg_management.dto.response.payment;

import com.gyan.pg_management.enums.PaymentMode;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class PaymentResponse {
    Long paymentId;
    Double amount;
    PaymentMode paymentMode;
    LocalDateTime paymentDate;
}

