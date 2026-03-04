package com.cts.eventsphere.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

/**
 * JwtUtil is a utility class responsible for generating and validating JWT tokens. It provides methods to create access and refresh tokens based on user email and role, as well as methods to extract user information from the token and validate its type. The class uses a secret key for signing the tokens and configurable expiration times for both access and refresh tokens.
 * * @author 2480010
 *
 * @version 1.0
 * @since 03-03-2026
 */
@Component
public class JwtUtil {
    @Value("${jwt.secret:nvjfenvjnjv53352434rnnc19dnwqdneciu439jn}")
    private String SECRET_KEY;

    @Value("${jwt.access.expiration-in-m:15}")
    private  long ACCESS_EXPIRATION_TIME_IN_M; // Default to 15 minutes

    @Value("${jwt.refresh.expiration-in-d:7}")
    private long REFRESH_EXPIRATION_TIME_IN_D; // Default to 7 days

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    private String buildToken(String email, String role, TokenType type) {
        var typeString = switch (type){
            case ACCESS -> "ACCESS";
            case REFRESH -> "REFRESH";
        };
        var expirationMillis = switch (type){
            case ACCESS -> ACCESS_EXPIRATION_TIME_IN_M * 60 * 1000;
            case REFRESH -> REFRESH_EXPIRATION_TIME_IN_D * 24 * 60 * 60 * 1000;
        };
        return Jwts.builder()
                .setSubject(email)
                .claim("email", email)
                .claim("role", role)
                .claim("type", typeString)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token, TokenType expectedType) {
        try {
            var claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            var tokenType = claims.getBody().get("type", String.class);
            return expectedType.name().equals(tokenType);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    UserPrincipal extractUserPrincipal(String token, TokenType expectedType) {
        var claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
        var tokenType = claims.getBody().get("type", String.class);
        if (!expectedType.name().equals(tokenType)) {
            throw new JwtException("Invalid token type. Expected: " + expectedType.name() + ", Found: " + tokenType);
        }
        String email = claims.getBody().get("email", String.class);
        String role = claims.getBody().get("role", String.class);
        return new UserPrincipal(email, role, List.of(new SimpleGrantedAuthority(role)));
    }

     public String generateAccessToken(String email, String role) {
         return buildToken(email, role, TokenType.ACCESS);
     }

     public String generateRefreshToken(String email, String role) {
         return buildToken(email, role, TokenType.REFRESH);
     }

}
