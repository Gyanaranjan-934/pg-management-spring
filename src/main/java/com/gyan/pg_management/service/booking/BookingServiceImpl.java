package com.gyan.pg_management.service.booking;

import com.gyan.pg_management.dto.request.booking.BookingCancelRequest;
import com.gyan.pg_management.dto.request.booking.BookingCheckoutRequest;
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
    public BookingResponse createBooking(BookingCreateRequest bookingCreateRequest) {

        // 1. Rule: start date validation
        if (bookingCreateRequest.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }

        // 2. Fetch dependencies
        Bed bed = bedService.getBed(bookingCreateRequest.getBedId());
        Tenant tenant = tenantService.getTenant(bookingCreateRequest.getTenantId());

        // 3. Rule: bed availability (Better to check bed's own status if possible)
        bookingRepository.findByBedAndStatus(bed, BookingStatus.ACTIVE)
                .ifPresent(b -> {
                    throw new IllegalStateException("Bed is already occupied by another tenant");
                });

        // 4. Rule: Tenant already has an active booking
        bookingRepository.findByTenantAndStatus(tenant, BookingStatus.ACTIVE)
                .ifPresent(booking -> {
                    throw new IllegalStateException("Tenant already has an active booking");
                });

        // 5. Build the Entity
        Booking booking = Booking.builder()
                .tenant(tenant)
                .bed(bed)
                .startDate(bookingCreateRequest.getStartDate())
                .monthlyRent(bookingCreateRequest.getMonthlyRent())
                .securityDeposit(bookingCreateRequest.getSecurityDeposit())
                .status(BookingStatus.ACTIVE)
                .build();

        // 6. IMPORTANT: Persist the new entity
        booking = bookingRepository.save(booking);

        // 7. Optional but recommended: Update Bed state if Bed entity has a status field
        // bed.setStatus(BedStatus.OCCUPIED);

        return BookingMapper.toResponse(booking);
    }
    @Transactional
    @Override
    public BookingResponse checkoutBooking(BookingCheckoutRequest bookingCheckoutRequest) {
        Booking booking = bookingRepository.findById(bookingCheckoutRequest.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Booking already completed");
        }

        Balance balance = balanceService.getOrCreateBalance(booking.getTenant());

        if (balanceService.hasPendingDues(booking.getTenant())) {
            throw new IllegalStateException("Outstanding dues exist. Clear before booking.");
        }
        booking.setStatus(BookingStatus.COMPLETED);
        booking.setEndDate(bookingCheckoutRequest.getCheckoutDate());

        booking = bookingRepository.save(booking);
        return BookingMapper.toResponse(booking);
    }

    @Transactional
    @Override
    public BookingResponse cancelBooking(BookingCancelRequest bookingCancelRequest) {

        Booking booking = bookingRepository.findById(bookingCancelRequest.getBookingId())
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

        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.toResponse(savedBooking);
    }

}
