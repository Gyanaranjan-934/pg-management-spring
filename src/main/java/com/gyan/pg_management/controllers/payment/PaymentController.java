package com.gyan.pg_management.controllers.payment;

import com.gyan.pg_management.dto.request.payment.CreatePaymentRequest;
import com.gyan.pg_management.dto.response.payment.PaymentResponse;
import com.gyan.pg_management.service.payments.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    public ResponseEntity<PaymentResponse> createPayment(@RequestBody CreatePaymentRequest paymentRequest){
        PaymentResponse response = paymentService.createPayment(paymentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
