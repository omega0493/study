package com.study.api.auth.controller;

import com.study.api.auth.dto.JwtTokenDto;
import com.study.api.auth.service.TokenService;
import com.study.infra.common.dto.ResponseDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/token")
public class TokenController {

    private final TokenService tokenService;

    @GetMapping
    ResponseEntity<ResponseDto> tokenRefresh(HttpServletRequest request) {
        String refreshToken = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        JwtTokenDto jwtTokenDto = tokenService.tokenRefresh(refreshToken);

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.AUTHORIZATION, "Bearer %s".formatted(jwtTokenDto.accessToken()));

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(new ResponseDto("200", "success", jwtTokenDto));
    }

}
