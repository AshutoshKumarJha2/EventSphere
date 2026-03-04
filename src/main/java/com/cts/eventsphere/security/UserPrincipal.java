package com.cts.eventsphere.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


/**
 * [Detailed description of the class's responsibility]
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
