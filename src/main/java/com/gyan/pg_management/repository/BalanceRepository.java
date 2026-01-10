package com.gyan.pg_management.repository;

import com.gyan.pg_management.entity.Balance;
import com.gyan.pg_management.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Optional<Balance> findByTenant(final Tenant tenant);
}
