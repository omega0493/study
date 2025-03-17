package com.study.infra.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationFailureHandlerTest {

    @BeforeEach
    void setUp() {
        sut = new CustomAuthenticationFailureHandler();
    }

    CustomAuthenticationFailureHandler sut;

    @Mock
    HttpServletResponse response;

    @Mock
    HttpServletRequest request;

    @Test
    void onAuthenticationFailure1() throws IOException {

        // given
        BadCredentialsException exception = new BadCredentialsException("Wrong token");
        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        // when
        sut.onAuthenticationFailure(request, response, exception);

        // then
        assertThat(stringWriter)
                .hasToString("{\"code\":\"user:auth:incorrect_credentials\"" +
                        ",\"message\":\"잘못된 인증 정보입니다.\"" +
                        ",\"data\":null}");
    }

    @Test
    void onAuthenticationFailure2() throws IOException {

        // given
        AuthenticationCredentialsNotFoundException exception = new AuthenticationCredentialsNotFoundException("Not found token");
        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        // when
        sut.onAuthenticationFailure(request, response, exception);

        // then
        assertThat(stringWriter)
                .hasToString("{\"code\":\"user:find:password_mismatch\"" +
                        ",\"message\":\"비밀번호가 일치하지 않습니다.\"" +
                        ",\"data\":null}");
    }
}
