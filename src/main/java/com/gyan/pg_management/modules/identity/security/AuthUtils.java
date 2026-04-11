package com.gyan.pg_management.modules.identity.security;

import com.gyan.pg_management.modules.identity.domain.User;
import com.gyan.pg_management.modules.identity.domain.AuthProviderType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class AuthUtils {
    @Value("${spring.jwt.secretKey}")
    private String jwtSecret;

    private SecretKey getSecretKey() {
        if (jwtSecret == null || jwtSecret.isBlank()) {
            log.error("JWT Secret Key is missing in properties file!");
            throw new IllegalStateException("JWT configuration error: Secret key is not properly loaded.");
        }
        log.debug("Generating SecretKey from provided jwtSecret.");
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        log.info("Generating Access Token for user: {}", user.getEmail());

        String token = Jwts.builder()
                .setSubject(null != user.getEmail() ? user.getEmail() : user.getUsername())
                .claim("userId", user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .signWith(getSecretKey())
                .compact();

        log.debug("Token successfully generated for userId: {}", user.getId());
        return token;
    }

    public String getUsernameFromToken(String token) {
        log.debug("Extracting username from JWT token...");
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String subject = claims.getSubject();
            log.info("Successfully extracted subject (email/username): {}", subject);
            return subject;
        } catch (Exception e) {
            log.error("Failed to parse JWT token: {}", e.getMessage());
            throw e;
        }
    }

    public AuthProviderType getProviderTypeFromRegistrationId(String registrationId) {
        log.info("Identifying provider type for registrationId: {}", registrationId);

        if (registrationId == null) {
            log.error("registrationId provided is null");
            throw new IllegalArgumentException("Provider identification failed: registrationId cannot be null.");
        }

        return switch (registrationId.toLowerCase()) {
            case "google" -> AuthProviderType.GOOGLE;
            case "github" -> AuthProviderType.GITHUB;
            default -> {
                log.error("Unknown or unsupported OAuth provider: {}", registrationId);
                throw new IllegalArgumentException("Authentication Error: The OAuth provider '" + registrationId + "' is not supported.");
            }
        };
    }

    public String determineProviderIdFromOAuth2User(OAuth2User oAuth2User, String registrationId) {
        log.debug("Extracting providerId for {} from OAuth2User attributes", registrationId);

        String providerId = switch (registrationId.toLowerCase()) {
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> {
                Object id = oAuth2User.getAttribute("id");
                yield (id != null) ? id.toString() : null;
            }
            default -> {
                log.error("Critical: Logic missing for provider identification of: {}", registrationId);
                throw new IllegalArgumentException("System Error: Unsupported OAuth2 provider encountered during ID extraction.");
            }
        };

        if (providerId == null || providerId.isBlank()) {
            log.error("Extraction Failed: Could not find unique ID (sub/id) for provider {}", registrationId);
            throw new IllegalArgumentException("Identity Error: Unable to determine unique provider ID from " + registrationId + " profile.");
        }

        log.info("Provider ID successfully determined: {}", providerId);
        return providerId;
    }

    public String determineUsernameFromOAuth2User(OAuth2User oAuth2User, String registrationId, String providerId) {
        log.debug("Determining display username for OAuth2 flow (Provider: {})", registrationId);

        String email = oAuth2User.getAttribute("email");
        if (email != null && !email.isBlank()) {
            log.info("Using email as username: {}", email);
            return email;
        }

        String username = switch (registrationId.toLowerCase()) {
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> oAuth2User.getAttribute("login");
            default -> providerId;
        };

        log.info("No email found. Using fallback username: {}", username);
        return username;
    }
}