package com.gyan.pg_management.service.balance;

import com.gyan.pg_management.dto.response.balance.BalanceResponse;
import com.gyan.pg_management.entity.Balance;
import com.gyan.pg_management.entity.Tenant;

public interface BalanceService {
    boolean hasPendingDues(Tenant tenant);
    Balance getOrCreateBalance(Tenant tenant);
    void addCharge(Tenant tenant, double amount, String reason);
    void applyPayment(Tenant tenant, Double amount);
    Balance getBalance(Tenant tenant);
}
