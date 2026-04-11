package com.gyan.pg_management.modules.billing.dto.response;

import com.gyan.pg_management.modules.billing.domain.PaymentMode;
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

