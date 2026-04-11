package com.gyan.pg_management.modules.identity.security;

import com.gyan.pg_management.modules.identity.domain.User;
import com.gyan.pg_management.modules.identity.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final AuthUtils authUtils;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
        log.info("Incoming request path in Filter chain: {}", path);

        // 1. Manually skip logic for OAuth2 and Auth endpoints
        if (path.startsWith("/oauth2/")) {
            log.info("Skipping JWT validation for OAuth2 path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String requestTokenHeader = request.getHeader("Authorization");

            if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
                log.debug("No valid Bearer token found in header for path: {}", path);
                filterChain.doFilter(request, response);
                return;
            }

            String token = requestTokenHeader.substring(7); // "Bearer " is 7 characters
            log.debug("Extracting token for validation...");

            String userData = authUtils.getUsernameFromToken(token);

            if (userData != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userRepository.findByUsernameOrEmail(userData)
                            .orElseThrow(() -> new RuntimeException("User not found with username/email: " + userData));

                log.info("Token validated for user: {}. Setting security context.", userData);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("JWT Filter Error at {}: {}", path, e.getMessage());
            // Stacktrace ke liye: log.error("Trace: ", e);
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
