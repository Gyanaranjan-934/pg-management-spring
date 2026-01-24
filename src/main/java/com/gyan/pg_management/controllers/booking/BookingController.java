package com.gyan.pg_management.controllers.booking;

import com.gyan.pg_management.dto.request.booking.*;
import com.gyan.pg_management.dto.response.booking.BookingResponse;
import com.gyan.pg_management.service.booking.BookingService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingCreateRequest request){
        log.info("REST request to create booking for Tenant: {} on Bed: {}", request.getTenantId(), request.getBedId());
        BookingResponse response = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(@Valid @RequestBody BookingCancelRequest request){
        log.info("REST request to cancel booking ID: {}", request.getBookingId());
        BookingResponse response = bookingService.cancelBooking(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/checkout") // Added '/' for consistency
    public ResponseEntity<BookingResponse> checkoutBooking(@Valid @RequestBody BookingCheckoutRequest request){
        log.info("REST request to checkout booking ID: {}", request.getBookingId());
        BookingResponse response = bookingService.checkoutBooking(request);
        return ResponseEntity.ok(response);
    }
}