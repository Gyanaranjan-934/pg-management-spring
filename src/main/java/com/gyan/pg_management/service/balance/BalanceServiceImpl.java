package com.gyan.pg_management.service.balance;

import com.gyan.pg_management.entity.Balance;
import com.gyan.pg_management.entity.Tenant;
import com.gyan.pg_management.exceptions.payment.ExcessPaymentException;
import com.gyan.pg_management.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;

    @Override
    public Balance getOrCreateBalance(Tenant tenant) {
        return balanceRepository.findByTenant(tenant)
                .orElseGet(() -> balanceRepository.save(
                        Balance.builder()
                                .tenant(tenant)
                                .outstandingAmount(0.0)
                                .totalPaid(0.0)
                                .build()
                ));
    }

    @Override
    public void addCharge(Tenant tenant, double amount, String reason) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Charge amount must be positive");
        }

        Balance balance = getOrCreateBalance(tenant);

        balance.setOutstandingAmount(
                balance.getOutstandingAmount() + amount
        );

        balanceRepository.save(balance);
    }

    @Override
    public void applyPayment(Tenant tenant, Double amount) {
        Balance balance = getOrCreateBalance(tenant);
        if (balance.getOutstandingAmount() < amount){
            throw new ExcessPaymentException(balance.getOutstandingAmount());
        }
        balance.setTotalPaid(balance.getTotalPaid() + amount);
        balance.setOutstandingAmount(balance.getOutstandingAmount() - amount);
        balanceRepository.save(balance);
    }

    @Override
    public Balance getBalance(Tenant tenant) {
        return balanceRepository.findByTenant(tenant).orElseThrow(()->new IllegalStateException("Balance not found"));
    }

    @Override
    public boolean hasPendingDues(Tenant tenant) {
        return balanceRepository.findByTenant(tenant)
                .map(b -> b.getOutstandingAmount() > 0)
                .orElse(false);
    }
}
