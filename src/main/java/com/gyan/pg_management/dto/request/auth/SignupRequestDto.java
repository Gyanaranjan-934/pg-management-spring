package com.gyan.pg_management.dto.request.auth;

import com.gyan.pg_management.enums.AuthProviderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
    String email;
    String password;
    String role;
    String username;
    AuthProviderType providerType;
    String providerId;
}
