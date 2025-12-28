package com.gyan.pg_management.service.booking;

import com.gyan.pg_management.entity.Bed;
import com.gyan.pg_management.entity.Booking;
import com.gyan.pg_management.entity.Tenant;

import java.time.LocalDate;

public interface BookingService {
    Booking createBooking(
            Tenant tenant,
            Bed bed,
            LocalDate startDate,
            Double monthlyRent,
            Double securityDeposit
    );
    Booking checkoutBooking(Long bookingId, LocalDate checkoutDate);
    Booking cancelBooking(Long bookingId);
}
