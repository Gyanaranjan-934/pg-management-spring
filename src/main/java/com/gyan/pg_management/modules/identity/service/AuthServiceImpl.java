package com.gyan.pg_management.modules.identity.service;

import com.gyan.pg_management.modules.identity.dto.request.LoginRequestDto;
import com.gyan.pg_management.modules.identity.dto.request.SignupRequestDto;
import com.gyan.pg_management.modules.identity.dto.response.LoginResponseDto;
import com.gyan.pg_management.modules.identity.dto.response.SignupResponseDto;
import com.gyan.pg_management.modules.identity.domain.User;
import com.gyan.pg_management.modules.identity.domain.AuthProviderType;
import com.gyan.pg_management.modules.identity.domain.Role;
import com.gyan.pg_management.modules.identity.repository.UserRepository;
import com.gyan.pg_management.modules.identity.security.AuthUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final AuthUtils authUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDto loginByEmailAndPassword(LoginRequestDto request) {
        log.info("Attempting login for email: {}", request.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        log.debug("Authentication successful for user: {}", request.getEmail());
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;

        assert user != null;
        String token = authUtils.generateAccessToken(user);

        log.info("JWT token generated successfully for userId: {}", user.getId());
        return LoginResponseDto.builder()
                .jwt(token)
                .userId(user.getId())
                .build();
    }

    public SignupResponseDto signupByEmailAndPassword(SignupRequestDto request) {
        log.info("Starting signup process for email: {}", request.getEmail());

        if (null == request.getEmail() || null == request.getPassword()) {
            log.error("Signup failed: Email or Password provided as null");
            throw new IllegalArgumentException("Signup validation failed: Email and Password are required fields.");
        }

        if (null == request.getRole()) {
            log.error("Signup failed: Role not specified for email: {}", request.getEmail());
            throw new IllegalArgumentException("Signup validation failed: User role (OWNER/MANAGER/TENANT) must be provided.");
        }

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            log.warn("Signup conflict: User with email {} already exists", request.getEmail());
            throw new IllegalArgumentException("Registration failed: An account is already associated with the provided email address.");
        }

        User newUser = userRepository.save(User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("OWNER".equals(request.getRole()) ? Role.OWNER :
                        "MANAGER".equals(request.getRole()) ? Role.MANAGER : Role.TENANT)
                .build()
        );

        log.info("Successfully registered new user with ID: {} and role: {}", newUser.getId(), newUser.getRole());
        return SignupResponseDto.builder()
                .id(newUser.getId())
                .email(newUser.getEmail())
                .build();
    }

    public User signupByOAuth2Username(SignupRequestDto request) {
        log.info("Initiating OAuth2 signup for username: {}", request.getUsername());

        if (null == request.getUsername()) {
            log.error("OAuth2 signup failed: Username is missing in request");
            throw new IllegalArgumentException("OAuth2 registration failed: Username attribute cannot be null.");
        }

        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            log.warn("OAuth2 signup conflict: Username {} is already taken", request.getUsername());
            throw new IllegalArgumentException("OAuth2 registration failed: The username is already in use.");
        }

        User savedUser = userRepository.save(User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .providerId(request.getProviderId())
                .role(Role.TENANT)
                .providerType(request.getProviderType())
                .build());

        log.info("OAuth2 user created successfully. ID: {}, Provider: {}", savedUser.getId(), savedUser.getProviderType());
        return savedUser;
    }

    @Override
    @Transactional
    public ResponseEntity<LoginResponseDto> handleOAuth2LoginRequest(OAuth2User user, String registrationId) {
        log.info("Processing OAuth2 login request from provider: {}", registrationId);

        AuthProviderType providerType = authUtils.getProviderTypeFromRegistrationId(registrationId);
        String providerId = authUtils.determineProviderIdFromOAuth2User(user, registrationId);

        log.debug("Provider Type: {}, Provider ID: {}", providerType, providerId);

        User oauthUser = userRepository.findByProviderTypeAndProviderId(providerType, providerId)
                .orElse(null);

        String email = user.getAttribute("email");
        log.debug("Extracted email from OAuth2 profile: {}", email);

        User emailUser = userRepository.findByEmail(email).orElse(null);

        if (null == oauthUser && emailUser == null) {
            log.info("First-time OAuth2 user detected. Proceeding with auto-signup for email: {}", email);
            String username = authUtils.determineUsernameFromOAuth2User(user, registrationId, providerId);
            oauthUser = signupByOAuth2Username(new SignupRequestDto(username, null, null, username, providerType, providerId));
        } else if (emailUser != null && oauthUser == null) {
            log.error("OAuth2 login failed: Email {} exists but is not linked to this OAuth provider", email);
            throw new BadCredentialsException("Security Alert: An account with this email already exists using a different sign-in method.");
        }

        String accessToken = authUtils.generateAccessToken(oauthUser);
        log.info("OAuth2 login successful for userId: {}", oauthUser.getId());

        return ResponseEntity.ok(LoginResponseDto.builder()
                .jwt(accessToken)
                .userId(oauthUser.getId())
                .build());
    }
}