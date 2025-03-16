package com.study.infra.security;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CustomAuthenticationFailureHandler.class)
class CustomAuthenticationFailureHandlerTest {

    @Mock
    HttpServletResponse httpServletResponse;

    @Test
    void onAuthenticationFailure() {
    }
}