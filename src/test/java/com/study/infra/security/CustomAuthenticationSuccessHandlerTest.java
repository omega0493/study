package com.study.infra.security;

import com.study.api.auth.constant.UserRole;
import com.study.api.auth.model.UserModel;
import com.study.api.auth.repository.UserRepository;
import com.study.api.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationSuccessHandlerTest {

    @InjectMocks
    private CustomAuthenticationSuccessHandler successHandler;

    @Mock
    private AuthService authService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    void onAuthenticationSuccess() throws IOException {

        // given
        UserModel model = UserModel.builder()
                .id(1L)
                .userName("foo")
                .userPassword("bar")
                .userRole(UserRole.USER)
                .build();

        when(authentication.getName()).thenReturn("1");
        when(authService.getUserById(1L)).thenReturn(model);

        // when
        successHandler.onAuthenticationSuccess(request, response, authentication);

        Authentication contextAuth = SecurityContextHolder.getContext().getAuthentication();

        // then
        assertThat(contextAuth).isNotNull();

        assertThat(contextAuth.getAuthorities().stream().map(GrantedAuthority::getAuthority))
                .containsExactly(String.valueOf(new SimpleGrantedAuthority(UserRole.USER.getCode())));

        assertThat(contextAuth.getPrincipal())
                .isEqualTo("foo");

        assertThat(contextAuth.getCredentials())
                .isEqualTo("bar");

        assertThat(contextAuth.getDetails())
                .isEqualTo(model);

    }
}