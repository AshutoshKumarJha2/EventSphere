package com.cts.eventsphere.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Collate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JwtFilter is a custom filter that intercepts incoming HTTP requests to validate JWT tokens. It checks the "Authorization" header for a Bearer token, extracts the user information from the token, and sets the authentication in the SecurityContext if the token is valid. If any exceptions occur during token validation, it logs the error and clears the SecurityContext.
 * * @author 2480010
 *
 * @version 1.0
 * @since 04-03-2026
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                UserPrincipal principal;
                if (request.getServletPath().equals("/api/v1/auth/refresh")) {
                    principal = jwtUtil.extractUserPrincipal(token, TokenType.REFRESH);
                } else {
                    principal = jwtUtil.extractUserPrincipal(token, TokenType.ACCESS);
                }
                log.info("Extracted jwt for user {} with role {}", principal.userId(), principal.authorities());
                var authToken = new UsernamePasswordAuthenticationToken(
                        principal, null,principal.authorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e){
            logger.error(e.getMessage());
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}
