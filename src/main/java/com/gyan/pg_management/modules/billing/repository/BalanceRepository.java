package com.gyan.pg_management.modules.billing.repository;

import com.gyan.pg_management.modules.billing.domain.Balance;
import com.gyan.pg_management.modules.identity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Optional<Balance> findByTenant(User tenant);
}
