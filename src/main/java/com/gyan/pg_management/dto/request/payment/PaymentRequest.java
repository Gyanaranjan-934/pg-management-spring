package com.gyan.pg_management.dto.request.payment;

import com.gyan.pg_management.enums.PaymentMode;
import lombok.Data;

@Data
public class PaymentRequest {
    private Long bookingId; // nullable
    private Long tenantId;
    private Double amount;
    private PaymentMode paymentMode;
    private String remarks;
}
