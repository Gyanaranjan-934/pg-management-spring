package com.gyan.pg_management.modules.identity.dto.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SignupResponseDto {
    Long id;
    String email;
    String username;
}
