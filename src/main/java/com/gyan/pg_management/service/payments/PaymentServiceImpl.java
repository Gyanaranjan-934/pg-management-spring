package com.gyan.pg_management.service.payments;

import com.gyan.pg_management.entity.Booking;
import com.gyan.pg_management.entity.Payment;
import com.gyan.pg_management.entity.Tenant;
import com.gyan.pg_management.enums.BookingStatus;
import com.gyan.pg_management.enums.PaymentMode;
import com.gyan.pg_management.repository.PaymentRepository;
import com.gyan.pg_management.service.balance.BalanceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final BalanceService balanceService;

    @Override
    public Payment recordPayment(
            Booking booking,
            Tenant tenant,
            Double amount,
            PaymentMode paymentMode,
            String remarks
    ){
        if (booking.getStatus() == BookingStatus.COMPLETED){
            throw new IllegalStateException("Cannot accept payment for completed booking");
        }
        double totalPaid = paymentRepository.findByBooking(booking)
                .stream()
                .mapToDouble(Payment::getAmount)
                .sum();
        double pending = booking.getMonthlyRent() - totalPaid;
        if (amount > pending) {
            throw new IllegalArgumentException("Payment exceeds pending dues");
        }

        Payment payment = Payment.builder()
                .booking(booking)
                .tenant(tenant)
                .amount(amount)
                .paymentDate(LocalDate.now())
                .paymentMode(paymentMode)
                .remarks(remarks)
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        balanceService.applyPayment(tenant,amount);
        return savedPayment;
    }
}
