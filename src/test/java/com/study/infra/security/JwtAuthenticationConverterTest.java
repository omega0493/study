package com.study.infra.security;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationConverterTest {

    private JwtAuthenticationConverter sut;

    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        sut = new JwtAuthenticationConverter();
    }

    @Test
    void validHeader() {

        // given
        String token = "eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiIyIiwiaWF0IjoxNzQxMTU2MzkyLCJleHAiOjE3NDExODUxOTJ9.bn795JRbZI0KSUMdWdJXPbhwd_DCSVnwbjmHNEuqHIEY6N452aNz06fHQ1Snpbyz";
        when(httpServletRequest.getHeader(any()))
                .thenReturn("Bearer " + token);

        // when
        Authentication convert = sut.convert(httpServletRequest);

        // then
        assertThat(convert)
                .isNotNull()
                .returns(token, Authentication::getCredentials);
    }

    @NullSource
    @ValueSource(strings = {
            "",
            " ",
            "Bearer ",
    })
    @ParameterizedTest
    void invalidHeader(String header) {

        // given
        when(httpServletRequest.getHeader(any()))
                .thenReturn(header);

        // when
        Authentication convert = sut.convert(httpServletRequest);

        //then
        assertThat(convert)
                .isNull();
    }
}
