package com.gyan.pg_management.service.balance;

import com.gyan.pg_management.entity.Tenant;

public interface BalanceService {
    boolean hasPendingDues(Tenant tenant);
    void initializeIfAbsent(Tenant tenant);
}
