package com.gyan.pg_management.repository;

import com.gyan.pg_management.entity.Booking;
import com.gyan.pg_management.entity.Bed;
import com.gyan.pg_management.entity.Tenant;
import com.gyan.pg_management.enums.BookingStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Booking> findByBedAndStatus(Bed bed, BookingStatus status);
    Optional<Booking> findByTenantAndStatus(Tenant tenant, BookingStatus status);
}
