package com.gyan.pg_management.repository;

import com.gyan.pg_management.entity.Booking;
import com.gyan.pg_management.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByBooking(Booking booking);
}
