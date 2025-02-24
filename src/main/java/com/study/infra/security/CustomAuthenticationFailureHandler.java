package com.study.infra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.infra.common.exception.BusinessError;
import com.study.infra.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 실패 이유 설정
        if (exception instanceof BadCredentialsException) {
            String json = new ObjectMapper().writeValueAsString(new BusinessException(BusinessError.INCORRECT_CREDENTIALS));
            response.getWriter().write(json);
        }

        // JSON 응답
        String json = new ObjectMapper().writeValueAsString(new BusinessException(BusinessError.PASSWORD_MISMATCH));
        response.getWriter().write(json);
    }
}
