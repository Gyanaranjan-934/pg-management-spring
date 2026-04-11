package com.gyan.pg_management.modules.billing.service;

import com.gyan.pg_management.modules.billing.domain.Balance;
import com.gyan.pg_management.modules.identity.domain.User;

public interface BalanceService {
    boolean hasPendingDues(User tenant);
    Balance getOrCreateBalance(User tenant);
    void addCharge(User tenant, double amount, String reason);
    void applyPayment(User tenant, Double amount);
    Balance getBalance(User tenant);
}
