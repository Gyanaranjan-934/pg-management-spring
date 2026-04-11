package com.gyan.pg_management.modules.billing.repository;

import com.gyan.pg_management.modules.booking.domain.Booking;
import com.gyan.pg_management.modules.billing.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByBooking(Booking booking);
}
