package com.gyan.pg_management.modules.billing.service;

import com.gyan.pg_management.modules.billing.dto.request.CreatePaymentRequest;
import com.gyan.pg_management.modules.billing.dto.response.PaymentResponse;
import com.gyan.pg_management.modules.booking.domain.Booking;
import com.gyan.pg_management.modules.billing.domain.Payment;
import com.gyan.pg_management.modules.identity.domain.User;
import com.gyan.pg_management.modules.booking.domain.BookingStatus;
import com.gyan.pg_management.modules.booking.exception.BookingNotFoundException;
import com.gyan.pg_management.modules.billing.mapper.PaymentMapper;
import com.gyan.pg_management.modules.billing.repository.BalanceRepository;
import com.gyan.pg_management.modules.booking.repository.BookingRepository;
import com.gyan.pg_management.modules.billing.repository.PaymentRepository;
import com.gyan.pg_management.modules.billing.service.BalanceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    private final BalanceService balanceService;
    private final BalanceRepository balanceRepository;

    @Override
    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest paymentRequest){
        Booking booking = bookingRepository.findById(paymentRequest.getBookingId())
                .orElseThrow(() -> new BookingNotFoundException(paymentRequest.getBookingId()));

        if (booking.getStatus() == BookingStatus.COMPLETED){
            throw new IllegalStateException("Cannot accept payment for completed booking");
        }

        User tenant = booking.getTenant();

        balanceService.applyPayment(tenant,paymentRequest.getAmount());

        Payment payment = Payment.builder()
                .booking(booking)
                .tenant(tenant)
                .amount(paymentRequest.getAmount())
                .paymentDate(paymentRequest.getPaymentDate())
                .paymentMode(paymentRequest.getPaymentMode())
                .remarks(paymentRequest.getRemarks())
                .build();

        payment = paymentRepository.save(payment);
        return PaymentMapper.toResponse(payment);
    }
}
