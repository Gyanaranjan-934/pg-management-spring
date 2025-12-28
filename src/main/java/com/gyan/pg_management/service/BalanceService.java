package com.gyan.pg_management.service;

import com.gyan.pg_management.entity.Balance;
import com.gyan.pg_management.entity.Tenant;
import com.gyan.pg_management.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;

    public boolean hasPendingDues(Tenant tenant) {
        return balanceRepository.findByTenant(tenant)
                .map(b -> b.getOutstandingAmount() > 0)
                .orElse(false);
    }

    public void initializeIfAbsent(Tenant tenant) {
        balanceRepository.findByTenant(tenant)
                .orElseGet(() -> balanceRepository.save(
                        Balance.builder()
                                .tenant(tenant)
                                .outstandingAmount(0.0)
                                .totalPaid(0.0)
                                .build()
                ));
    }
}
