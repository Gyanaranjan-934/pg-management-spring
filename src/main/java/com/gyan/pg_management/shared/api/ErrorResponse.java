package com.gyan.pg_management.shared.api;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Value
@Builder
public class ErrorResponse {
    Integer status;
    String error;
    String message;
    LocalDateTime timestamp;
}
