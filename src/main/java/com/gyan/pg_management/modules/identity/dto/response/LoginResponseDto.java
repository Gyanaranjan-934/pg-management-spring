package com.gyan.pg_management.modules.identity.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginResponseDto {
    String jwt;
    Long userId;
}
