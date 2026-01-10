package com.gyan.pg_management.controllers.booking;

import com.gyan.pg_management.dto.request.booking.*;
import com.gyan.pg_management.dto.response.booking.BookingResponse;
import com.gyan.pg_management.service.booking.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingCreateRequest request){
        BookingResponse response = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(@RequestBody BookingCancelRequest request){
        BookingResponse response = bookingService.cancelBooking(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("checkout")
    public ResponseEntity<BookingResponse> checkoutBooking(@RequestBody BookingCheckoutRequest request){
        BookingResponse response = bookingService.checkoutBooking(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
