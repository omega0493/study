package com.study.infra.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = JwtAuthenticationProvider.class)
class JwtAuthenticationProviderTest {

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @MockitoBean
    private JwtProvider jwtProvider;

    @Mock
    private Authentication authentication;

    @CsvSource(textBlock = """
            org.springframework.security.authentication.UsernamePasswordAuthenticationToken | true
            java.lang.Object                                                                | false
            """, delimiter = '|')
    @ParameterizedTest
    void supports(Class<?> clazz, boolean expected) {

        // when
        boolean supports = jwtAuthenticationProvider.supports(clazz);

        // then
        assertThat(supports).isEqualTo(expected);
    }

    @Test
    void authenticate() {

        // given
        when(authentication.getCredentials()).thenReturn("");
        when(jwtProvider.getUsernameFromToken(any())).thenReturn("foo");

        // when
        Authentication authenticate = jwtAuthenticationProvider.authenticate(authentication);

        // then
        assertThat(authenticate)
                .returns("foo", Authentication::getPrincipal)
                .matches(Authentication::isAuthenticated)
                .returns(null, Authentication::getCredentials)
                .returns(List.of(), Authentication::getAuthorities);
    }

    @Test
    void wrongTokenAuthenticate() {

        // given
        when(authentication.getCredentials()).thenReturn("");
        when(jwtProvider.getUsernameFromToken(any())).thenReturn("");

        // when
        assertThatException()
                .isThrownBy(() -> jwtAuthenticationProvider.authenticate(authentication))
                .isInstanceOf(BadCredentialsException.class)
                .withMessage("Wrong token");
    }

    @Test
    void notFoundTokenAuthenticate() {

        // given
        when(authentication.getCredentials()).thenReturn(null);

        // when
        assertThatException()
                .isThrownBy(() -> jwtAuthenticationProvider.authenticate(authentication))
                .isInstanceOf(AuthenticationCredentialsNotFoundException.class)
                .withMessage("Not found token");
    }
}
