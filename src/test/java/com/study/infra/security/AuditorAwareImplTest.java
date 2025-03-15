package com.study.infra.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditorAwareImplTest {

    private AuditorAwareImpl sut;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        sut = new AuditorAwareImpl();
    }

    @Test
    void getCurrentAuditorTest() {

        // given
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("1");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        Optional<String> maybeCurrentAuditor = sut.getCurrentAuditor();

        // then
        assertThat(maybeCurrentAuditor)
                .isNotNull()
                .isPresent()
                .hasValue("1");
    }

    @Test
    void returnEmptyWhenNoAuthenticatedUser() {
        // when
        Optional<String> maybeCurrentAuditor = sut.getCurrentAuditor();

        // then
        assertThat(maybeCurrentAuditor)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void returnEmptyWhenUserIsUnauthenticated() {

        // given
        when(authentication.isAuthenticated()).thenReturn(false);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        Optional<String> maybeCurrentAuditor = sut.getCurrentAuditor();

        // then
        assertThat(maybeCurrentAuditor)
                .isNotNull()
                .isEmpty();
    }

}
