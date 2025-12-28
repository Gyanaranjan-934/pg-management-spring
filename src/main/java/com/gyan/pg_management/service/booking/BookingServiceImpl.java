package com.gyan.pg_management.service.booking;

import com.gyan.pg_management.entity.*;
import com.gyan.pg_management.enums.BookingStatus;
import com.gyan.pg_management.repository.BookingRepository;
import com.gyan.pg_management.repository.PaymentRepository;
import com.gyan.pg_management.service.balance.BalanceServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final BalanceServiceImpl balanceService;

    @Transactional
    @Override
    public Booking createBooking(
            Tenant tenant,
            Bed bed,
            LocalDate startDate,
            Double monthlyRent,
            Double securityDeposit
    ) {

        // Rule: start date validation
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }

        // Rule: bed availability
        bookingRepository.findByBedAndStatus(bed, BookingStatus.ACTIVE)
                .ifPresent(b -> {
                    throw new IllegalStateException("Bed is already occupied");
                });

        // Rule: Tenant has already active booking or not
        bookingRepository.findByTenantAndStatus(tenant, BookingStatus.ACTIVE)
                .ifPresent(booking -> {
                    throw new IllegalStateException("Tenant already having active booking");
                });


        Booking booking = Booking.builder()
                .tenant(tenant)
                .bed(bed)
                .startDate(startDate)
                .monthlyRent(monthlyRent)
                .securityDeposit(securityDeposit)
                .status(BookingStatus.ACTIVE)
                .build();

        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public Booking checkoutBooking(Long bookingId, LocalDate checkoutDate) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Booking already completed");
        }

        // Ensure balance exists
        balanceService.initializeIfAbsent(booking.getTenant());

        // Due check
        if (balanceService.hasPendingDues(booking.getTenant())) {
            throw new IllegalStateException("Outstanding dues exist. Clear before booking.");
        }

        if (checkoutDate.isBefore(LocalDate.now())){
            throw new IllegalStateException("Checkout date cannot be before current date");
        }

        booking.setStatus(BookingStatus.COMPLETED);
        booking.setEndDate(LocalDate.now());

        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public Booking cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // Rule 1: Already completed
        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Completed booking cannot be cancelled");
        }

        // Rule 2: Already cancelled
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking already cancelled");
        }

        // Rule 3: Stay already started
        if (!booking.getStartDate().isAfter(LocalDate.now())) {
            throw new IllegalStateException(
                    "Booking cannot be cancelled after stay has started"
            );
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setEndDate(LocalDate.now()); // audit purpose

        return bookingRepository.save(booking);
    }

}
