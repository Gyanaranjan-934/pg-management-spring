package com.gyan.pg_management.dto.response.balance;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BalanceResponse {
    Long balanceId;
    Long tenantId;
    Double totalPaid;
    Double outstandingAmount;
}
