package com.cts.eventsphere.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


/**
 * UserPrincipal is a record that represents the authenticated user's principal information, including their email, role, and authorities. It is used in the security context to manage user authentication and authorization.
 * * @author 2480010
 *
 * @version 1.0
 * @since 04-03-2026
 */
public record UserPrincipal(
        String email,
        String role,
        Collection<? extends GrantedAuthority> authorities
) {
}
