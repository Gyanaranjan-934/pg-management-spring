package com.gyan.pg_management.service.booking;

import com.gyan.pg_management.dto.request.booking.BookingCreateRequest;
import com.gyan.pg_management.dto.response.booking.BookingResponse;
import com.gyan.pg_management.entity.*;
import com.gyan.pg_management.enums.BookingStatus;
import com.gyan.pg_management.mapper.BookingMapper;
import com.gyan.pg_management.repository.BookingRepository;
import com.gyan.pg_management.repository.PaymentRepository;
import com.gyan.pg_management.service.balance.BalanceService;
import com.gyan.pg_management.service.balance.BalanceServiceImpl;
import com.gyan.pg_management.service.bed.BedService;
import com.gyan.pg_management.service.tenant.TenantService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final BalanceService balanceService;
    private final BedService bedService;
    private final TenantService tenantService;

    @Transactional
    @Override
    @NonNull
    public BookingResponse createBooking(BookingCreateRequest bookingCreateRequest) {

        // Rule: start date validation
        if (bookingCreateRequest.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }

        Bed bed = bedService.getBed(bookingCreateRequest.getBedId());

        // Rule: bed availability
        bookingRepository.findByBedAndStatus(bed, BookingStatus.ACTIVE)
                .ifPresent(b -> {
                    throw new IllegalStateException("Bed is already occupied");
                });

        Tenant tenant = tenantService.getTenant(bookingCreateRequest.getTenantId());
        // Rule: Tenant has already active booking or not
        bookingRepository.findByTenantAndStatus(tenant, BookingStatus.ACTIVE)
                .ifPresent(booking -> {
                    throw new IllegalStateException("Tenant already having active booking");
                });


        Booking booking = Booking.builder()
                .tenant(tenant)
                .bed(bed)
                .startDate(bookingCreateRequest.getStartDate())
                .monthlyRent(bookingCreateRequest.getMonthlyRent())
                .securityDeposit(bookingCreateRequest.getSecurityDeposit())
                .status(BookingStatus.ACTIVE)
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.toResponse(savedBooking);
    }

    @Transactional
    @Override
    public Booking checkoutBooking(Long bookingId, LocalDate checkoutDate) {
        Objects.requireNonNull(bookingId,"BookingId must not be null");
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Booking already completed");
        }

        // Ensure balance exists
        Balance balance = balanceService.getOrCreateBalance(booking.getTenant());

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
