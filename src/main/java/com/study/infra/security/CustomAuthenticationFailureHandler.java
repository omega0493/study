package com.study.infra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.infra.common.dto.ResponseDto;
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
        BusinessError businessError;
        if (exception instanceof BadCredentialsException) {
            businessError = BusinessError.INCORRECT_CREDENTIALS;
        } else {
            businessError = BusinessError.PASSWORD_MISMATCH;
        }

        ResponseDto body = new ResponseDto(businessError.getCode(), businessError.getMessage(), null);
        String json = new ObjectMapper().writeValueAsString(body);
        response.getWriter().write(json);
    }

}
