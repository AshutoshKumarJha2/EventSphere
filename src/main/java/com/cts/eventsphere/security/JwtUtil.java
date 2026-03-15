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

    /**
    * Retrieves the signing key used for generating and validating JWT tokens. The key is derived from the SECRET_KEY property, which is injected from the application configuration. The method returns a SecretKey object that can be used for signing and verifying JWT tokens.
     * * @author 2480010
     * @version 1.0
     * @since 03-03-2026
    * * @param none
    * @return SecretKey
    */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
    * Generates a JWT token based on the provided user information and token type. The method constructs a JWT token with claims for userId, email, role, and token type, and sets the issued and expiration times based on the token type (access or refresh). The token is signed using the HS256 algorithm and returned as a compact string.
     * * @author 2480010
     * @version 1.0
     * @since 03-03-2026
    * * @param userId The unique identifier of the user for whom the token is being generated.
    * @param email The email address of the user, which is set as the subject of the token.
    * @param role The role of the user (e.g., USER, ADMIN), which is included as a claim in the token.
    * @param type The type of token
    * @return A JWT token as a compact string that can be used for authentication and authorization purposes.
    */
    private String buildToken(String userId, String email, String role, TokenType type) {
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
                .claim("userId", userId)
                .claim("email", email)
                .claim("role", role)
                .claim("type", typeString)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
    * Extracts the email (subject) from the provided JWT token. The method parses the token using the signing key and retrieves the subject claim, which contains the email address of the user. If the token is valid, it returns the email as a string; otherwise, it may throw an exception if the token is invalid or expired.
     * * @author 2480010
     * @version 1.0
     * @since 03-03-2026
     * @throws JwtException if the token is invalid or cannot be parsed.
    * * @param token The JWT token from which the email is to be extracted.
    * @return The email address of the user extracted from the token's subject claim.
    */
    public String extractEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
    * Validates the provided JWT token against the expected token type. The method parses the token using the signing key and checks if the "type" claim in the token matches the expected token type (e.g., ACCESS or REFRESH). If the token is valid and the type matches, it returns true; otherwise, it returns false. The method also handles exceptions that may occur during token parsing, such as invalid tokens or expired tokens.
     * * @author 2480010
     * @version 1.0
     * @since 03-03-2026
     * @throws JwtException if the token is invalid or cannot be parsed.
     * @throws IllegalArgumentException if the token is null or empty.
    * * @param token The JWT token to be validated.
    * @param expectedType The expected type of the token (e.g., ACCESS or REF)RESH) that the token should match for it to be considered valid.
    * @return true if the token is valid and matches the expected type; false otherwise.
    */
    public boolean validateToken(String token, TokenType expectedType) {
        try {
            var claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            var tokenType = claims.getBody().get("type", String.class);
            return expectedType.name().equals(tokenType);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
    * Extracts user information from the provided JWT token and constructs a UserPrincipal object. The method parses the token using the signing key, checks if the "type" claim matches the expected token type, and retrieves the email, role, and userId claims from the token. It then creates a UserPrincipal object with the extracted information and returns it. If the token is invalid or the type does not match, it throws a JwtException.
     * * @author 2480010
     * @version 1.0
     * @since 03-03-2026
     * @throws JwtException if the token is invalid, cannot be parsed, or if the token type does not match the expected type.
    * * @param token The JWT token from which user information is to be extracted.
    * @param expectedType The expected type of the token (e.g., ACCESS or REFRESH) that the token should match for the extraction to proceed.
     * @throws JwtException if the token is invalid, cannot be parsed, or if the token type does not match the expected type
    * @return A UserPrincipal object containing the user's unique identifier, email, role, and authorities extracted from the token.
    */
    UserPrincipal extractUserPrincipal(String token, TokenType expectedType) {
        var claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
        var tokenType = claims.getBody().get("type", String.class);
        if (!expectedType.name().equals(tokenType)) {
            throw new JwtException("Invalid token type. Expected: " + expectedType.name() + ", Found: " + tokenType);
        }
        String email = claims.getBody().get("email", String.class);
        String role = claims.getBody().get("role", String.class);
        String userId = claims.getBody().get("userId", String.class);
        String roleAuthority = "ROLE_" + role.toUpperCase();
        return new UserPrincipal(userId, email, role, List.of(new SimpleGrantedAuthority(roleAuthority)));
    }

    /**
    * Generates an access token for the specified user. The method takes the user's unique identifier (userId), email, and role as parameters and calls the buildToken method to create a JWT token with the appropriate claims and expiration time for an access token. The generated access token is returned as a string.
     * * @author 2480010
     * @version 1.0
     * @since 03-03-2026
    * * @param userId The unique identifier of the user for whom the access token is being generated.
    * @param email The email address of the user, which is included as a claim in the token.
    * @param role The role of the user (e.g., USER, ADMIN
    * @return A JWT access token as a compact string that can be used for authenticating API requests and authorizing access to protected resources based on the user's role and permissions.
    */
     public String generateAccessToken(String userId, String email, String role) {
         return buildToken(userId, email, role, TokenType.ACCESS);
     }

     /**
     * Generates a refresh token for the specified user. The method takes the user's unique identifier (userId), email, and role as parameters and calls the buildToken method to create a JWT token with the appropriate claims and expiration time for a refresh token. The generated refresh token is returned as a string.
     * * @param userId The unique identifier of the user for whom the refresh token is being generated.
     * @param email The email address of the user, which is included as a claim in the token.
     * @param role The role of the user (e.g., USER, ADMIN
     * @return A JWT refresh token as a compact string that can be used for obtaining new access tokens without requiring the user to log in again.
      * @author 2480010
      * @version 1.0
      * @since 03-03-2026
     */
     public String generateRefreshToken(String userId, String email, String role) {
         return buildToken(userId, email, role, TokenType.REFRESH);
     }

}
