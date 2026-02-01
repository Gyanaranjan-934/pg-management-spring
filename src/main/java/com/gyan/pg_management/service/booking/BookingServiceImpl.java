package com.gyan.pg_management.service.booking;

import com.gyan.pg_management.dto.request.booking.BookingCancelRequest;
import com.gyan.pg_management.dto.request.booking.BookingCheckoutRequest;
import com.gyan.pg_management.dto.request.booking.BookingCreateRequest;
import com.gyan.pg_management.dto.response.booking.BookingResponse;
import com.gyan.pg_management.entity.*;
import com.gyan.pg_management.enums.BedStatus;
import com.gyan.pg_management.enums.BookingStatus;
import com.gyan.pg_management.exceptions.user.UserNotFoundException;
import com.gyan.pg_management.mapper.BookingMapper;
import com.gyan.pg_management.repository.BedRepository;
import com.gyan.pg_management.repository.BookingRepository;
import com.gyan.pg_management.repository.UserRepository;
import com.gyan.pg_management.service.balance.BalanceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BalanceService balanceService;
    private final BedRepository bedRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public BookingResponse createBooking(BookingCreateRequest request) {
        log.info("Starting booking creation process for Tenant: {}", request.getTenantId());

        // 1. Fetching Entities
        Bed bed = bedRepository.findById(request.getBedId())
                    .orElseThrow(()->new IllegalArgumentException("Bed Not found"));

        User tenant = userRepository.findById(request.getTenantId())
                    .orElseThrow(()->new UserNotFoundException("Tenant not found"));

        // 2. Business Rules Validations
        log.debug("Checking availability for Bed ID: {}", request.getBedId());
        bookingRepository.findByBedAndStatus(bed, BookingStatus.ACTIVE)
                .ifPresent(b -> {
                    log.error("Validation Failed: Bed {} is already occupied", request.getBedId());
                    throw new IllegalStateException("Bed is already occupied by another tenant");
                });

        log.debug("Checking if Tenant {} has any active bookings", request.getTenantId());
        bookingRepository.findByTenantAndStatus(tenant, BookingStatus.ACTIVE)
                .ifPresent(b -> {
                    log.error("Validation Failed: Tenant {} already has an active booking", request.getTenantId());
                    throw new IllegalStateException("Tenant already has an active booking");
                });

        // 3. Build & Save
        Booking booking = Booking.builder()
                .tenant(tenant)
                .bed(bed)
                .startDate(request.getStartDate())
                .monthlyRent(request.getMonthlyRent())
                .securityDeposit(request.getSecurityDeposit())
                .status(BookingStatus.ACTIVE)
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        log.info("Booking created successfully with ID: {}", savedBooking.getId());

        return BookingMapper.toResponse(savedBooking);
    }

    @Transactional
    @Override
    public BookingResponse checkoutBooking(BookingCheckoutRequest request) {
        log.info("Processing checkout for Booking ID: {}", request.getBookingId());

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + request.getBookingId()));

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            log.warn("Checkout skipped: Booking {} is already COMPLETED", request.getBookingId());
            throw new IllegalStateException("Booking already completed");
        }

        // Dues Check
        if (balanceService.hasPendingDues(booking.getTenant())) {
            log.error("Checkout blocked: Tenant {} has pending dues", booking.getTenant().getId());
            throw new IllegalStateException("Outstanding dues exist. Clear before checkout.");
        }

        booking.setStatus(BookingStatus.COMPLETED);
        booking.setEndDate(request.getCheckoutDate());
        booking.getBed().setStatus(BedStatus.VACANT);

        log.info("Checkout successful for Booking ID: {} on Date: {}", request.getBookingId(), request.getCheckoutDate());
        return BookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Transactional
    @Override
    public BookingResponse cancelBooking(BookingCancelRequest request) {
        log.info("Processing cancellation for Booking ID: {}", request.getBookingId());

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() != BookingStatus.ACTIVE) {
            log.error("Cancellation failed: Booking {} is in status {}", request.getBookingId(), booking.getStatus());
            throw new IllegalStateException("Only active bookings can be cancelled");
        }

        // Logic: Cannot cancel if stay has already started
        if (!booking.getStartDate().isAfter(LocalDate.now())) {
            log.warn("Cancellation rejected: Stay has already started for Booking {}", request.getBookingId());
            throw new IllegalStateException("Stay has already started. Use Checkout instead.");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setEndDate(LocalDate.now());

        log.info("Booking ID: {} cancelled successfully", request.getBookingId());
        return BookingMapper.toResponse(bookingRepository.save(booking));
    }
}