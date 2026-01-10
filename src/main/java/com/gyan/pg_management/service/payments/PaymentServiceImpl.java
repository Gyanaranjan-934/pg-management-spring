package com.gyan.pg_management.service.payments;

import com.gyan.pg_management.dto.request.payment.CreatePaymentRequest;
import com.gyan.pg_management.dto.response.payment.PaymentResponse;
import com.gyan.pg_management.entity.Balance;
import com.gyan.pg_management.entity.Booking;
import com.gyan.pg_management.entity.Payment;
import com.gyan.pg_management.entity.Tenant;
import com.gyan.pg_management.enums.BookingStatus;
import com.gyan.pg_management.enums.PaymentMode;
import com.gyan.pg_management.exceptions.booking.BookingNotFoundException;
import com.gyan.pg_management.exceptions.payment.ExcessPaymentException;
import com.gyan.pg_management.mapper.PaymentMapper;
import com.gyan.pg_management.repository.BalanceRepository;
import com.gyan.pg_management.repository.BookingRepository;
import com.gyan.pg_management.repository.PaymentRepository;
import com.gyan.pg_management.repository.TenantRepository;
import com.gyan.pg_management.service.balance.BalanceService;
import com.gyan.pg_management.service.booking.BookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    private final BalanceService balanceService;
    private final TenantRepository tenantRepository;
    private final BalanceRepository balanceRepository;

    @Override
    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest paymentRequest){
        Booking booking = bookingRepository.findById(paymentRequest.getBookingId())
                .orElseThrow(() -> new BookingNotFoundException(paymentRequest.getBookingId()));

        if (booking.getStatus() == BookingStatus.COMPLETED){
            throw new IllegalStateException("Cannot accept payment for completed booking");
        }

        Tenant tenant = booking.getTenant();

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
