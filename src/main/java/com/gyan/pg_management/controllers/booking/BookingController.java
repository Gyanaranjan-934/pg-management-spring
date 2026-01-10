package com.gyan.pg_management.controllers.booking;

import com.gyan.pg_management.dto.request.booking.BookingCreateRequest;
import com.gyan.pg_management.dto.response.booking.BookingResponse;
import com.gyan.pg_management.entity.Bed;
import com.gyan.pg_management.entity.Booking;
import com.gyan.pg_management.entity.Tenant;
import com.gyan.pg_management.mapper.BookingMapper;
import com.gyan.pg_management.service.bed.BedService;
import com.gyan.pg_management.service.booking.BookingService;
import com.gyan.pg_management.service.tenant.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
