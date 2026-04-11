package com.gyan.pg_management.modules.booking.repository;

import com.gyan.pg_management.modules.booking.domain.Booking;
import com.gyan.pg_management.modules.inventory.domain.Bed;
import com.gyan.pg_management.modules.identity.domain.User;
import com.gyan.pg_management.modules.booking.domain.BookingStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Booking> findByBedAndStatus(Bed bed, BookingStatus status);
    Optional<Booking> findByTenantAndStatus(User tenant, BookingStatus status);
}
