package com.gyan.pg_management.service;

import com.gyan.pg_management.entity.*;
import com.gyan.pg_management.enums.BookingStatus;
import com.gyan.pg_management.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

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
}
