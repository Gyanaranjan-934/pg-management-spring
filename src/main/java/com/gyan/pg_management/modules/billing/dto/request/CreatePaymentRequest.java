package com.gyan.pg_management.modules.billing.dto.request;

import com.gyan.pg_management.modules.billing.domain.PaymentMode;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
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