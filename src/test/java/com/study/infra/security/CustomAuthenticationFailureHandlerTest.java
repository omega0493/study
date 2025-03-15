package com.study.infra.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CustomAuthenticationFailureHandler.class)
class CustomAuthenticationFailureHandlerTest {

    @Test
    void onAuthenticationFailure() {
    }
}