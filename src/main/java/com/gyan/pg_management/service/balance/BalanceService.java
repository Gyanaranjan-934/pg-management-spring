package com.gyan.pg_management.service.balance;

import com.gyan.pg_management.entity.Balance;
import com.gyan.pg_management.entity.User;

public interface BalanceService {
    boolean hasPendingDues(User tenant);
    Balance getOrCreateBalance(User tenant);
    void addCharge(User tenant, double amount, String reason);
    void applyPayment(User tenant, Double amount);
    Balance getBalance(User tenant);
}
