package com.study.infra.security;

import com.study.api.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtProvider jwtProvider;

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getCredentials() instanceof String jwtToken) {
            String userId = jwtProvider.getUsernameFromToken(jwtToken);


            if (!StringUtils.hasText(userId)) {
                throw new BadCredentialsException("Wrong token");
            }

            return new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
        }

        throw new AuthenticationCredentialsNotFoundException("Not found token");
    }

}
