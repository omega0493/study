package com.study.infra.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // 인증된 사용자 정보 가져오기
        String username = authentication.getName();

        // Access Token & Refresh Token 생성
        String accessToken = jwtProvider.generateAccessToken(username);
        String refreshToken = jwtProvider.generateRefreshToken(username);

        // Refresh Token을 쿠키에 저장 (javax.servlet.http.Cookie 사용)
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7일 유지

        // SameSite 설정을 추가하려면 직접 헤더 추가
//        response.addHeader("Set-Cookie", "refresh_token=" + refreshToken + "; Path=/; HttpOnly; Secure; SameSite=Strict");
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(accessToken));

        response.addCookie(refreshTokenCookie);

        // JSON 응답 (Access Token 반환)
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

//        Map<String, String> tokens = new HashMap<>();
//        tokens.put("access_token", accessToken);
//
//        String json = new ObjectMapper().writeValueAsString(tokens);
//        response.getWriter().write(json);
    }

}
