package com.gyan.pg_management.service.auth;

import com.gyan.pg_management.dto.request.auth.LoginRequestDto;
import com.gyan.pg_management.dto.request.auth.SignupRequestDto;
import com.gyan.pg_management.dto.response.auth.LoginResponseDto;
import com.gyan.pg_management.dto.response.auth.SignupResponseDto;
import com.gyan.pg_management.entity.User;
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