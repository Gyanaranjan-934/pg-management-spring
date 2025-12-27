package com.gyan.pg_management.repository;

import com.gyan.pg_management.entity.Booking;
import com.gyan.pg_management.entity.Bed;
import com.gyan.pg_management.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBedAndStatus(Bed bed, BookingStatus status);
}
