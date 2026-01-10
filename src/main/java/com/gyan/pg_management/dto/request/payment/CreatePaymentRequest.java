package com.gyan.pg_management.dto.request.payment;

import com.gyan.pg_management.enums.PaymentMode;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreatePaymentRequest {

    @NotNull(message = "Booking ID must not be null")
    private Long bookingId;

    @NotNull(message = "Payment amount cannot be null")
    @Positive(message = "Payment amount must be greater than zero")
    private Double amount;

    @NotNull(message = "Payment mode is mandatory")
    private PaymentMode paymentMode;

    @NotNull(message = "Payment date is mandatory")
    @PastOrPresent(message = "Payment date cannot be in the future")
    private LocalDate paymentDate;

    private String remarks;
}