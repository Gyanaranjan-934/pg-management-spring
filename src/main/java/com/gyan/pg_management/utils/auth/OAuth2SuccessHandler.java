package com.gyan.pg_management.utils.auth;

import com.gyan.pg_management.dto.response.auth.LoginResponseDto;
import com.gyan.pg_management.service.auth.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final AuthService authService;
    private final ObjectMapper objectMapper;
    private final String oauth2RedirectUri;

    public OAuth2SuccessHandler(AuthService authService, ObjectMapper objectMapper, @Value("${app.oauth2.redirect-uri:}") String oauth2RedirectUri) {
        this.authService = authService;
        this.objectMapper = objectMapper;
        this.oauth2RedirectUri = oauth2RedirectUri;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Authentication successful. Extracting user details...");

        try {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            log.info("token from OAuth: {}",token);
            OAuth2User user = (OAuth2User) token.getPrincipal();
            String registrationId = token.getAuthorizedClientRegistrationId();

            log.info("Successfully authenticated via provider: {}", registrationId);
            log.debug("OAuth2 User attributes: {}", user.getAttributes());

            // Handing over to AuthService to manage user creation/login logic
            ResponseEntity<LoginResponseDto> loginResponse = authService.handleOAuth2LoginRequest(user, registrationId);

            log.info("AuthService processed OAuth2 request. Status: {}", loginResponse.getStatusCode());

            if (shouldReturnJson(request) || oauth2RedirectUri == null || oauth2RedirectUri.isBlank()) {
                // Writing JSON response to API clients (Postman, etc.)
                response.setStatus(loginResponse.getStatusCode().value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                if (loginResponse.getBody() != null) {
                    String jsonResponse = objectMapper.writeValueAsString(loginResponse.getBody());
                    response.getWriter().write(jsonResponse);
                    log.debug("Response body written to output stream successfully.");
                } else {
                    log.warn("OAuth2 login completed but response body was empty.");
                }
                return;
            }

            LoginResponseDto body = loginResponse.getBody();
            if (body == null) {
                log.warn("OAuth2 login completed but response body was empty; falling back to JSON response.");
                response.setStatus(loginResponse.getStatusCode().value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write("{}");
                return;
            }

            String redirectUrl = UriComponentsBuilder.fromUriString(oauth2RedirectUri)
                    .queryParam("token", body.getJwt())
                    .queryParam("userId", body.getUserId())
                    .build()
                    .toUriString();

            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            log.error("Critical error during OAuth2 success handling: {}", e.getMessage(), e);

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"error\": \"Authentication success handler failed to process the user request.\"}");
        }
    }

    private boolean shouldReturnJson(HttpServletRequest request) {
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains(MediaType.APPLICATION_JSON_VALUE)) {
            return true;
        }
        String responseType = request.getParameter("responseType");
        return responseType != null && responseType.equalsIgnoreCase("json");
    }
}
