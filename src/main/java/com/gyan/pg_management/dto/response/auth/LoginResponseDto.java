package com.gyan.pg_management.dto.response.auth;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginResponseDto {
    String jwt;
    Long userId;
}
