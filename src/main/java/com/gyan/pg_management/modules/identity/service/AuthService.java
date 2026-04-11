package com.gyan.pg_management.modules.identity.service;

import com.gyan.pg_management.modules.identity.dto.request.LoginRequestDto;
import com.gyan.pg_management.modules.identity.dto.request.SignupRequestDto;
import com.gyan.pg_management.modules.identity.dto.response.LoginResponseDto;
import com.gyan.pg_management.modules.identity.dto.response.SignupResponseDto;
import com.gyan.pg_management.modules.identity.domain.User;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

public interface AuthService {
    LoginResponseDto loginByEmailAndPassword(LoginRequestDto request);
    SignupResponseDto signupByEmailAndPassword(SignupRequestDto request);
    User signupByOAuth2Username(SignupRequestDto request);
    ResponseEntity<LoginResponseDto> handleOAuth2LoginRequest(OAuth2User user, String registrationId);
}