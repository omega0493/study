package com.study.api.auth.controller;

import com.study.api.auth.dto.LoginDto;
import com.study.api.auth.dto.LoginResponseDto;
import com.study.api.auth.model.UserModel;
import com.study.api.auth.service.AuthService;
import com.study.infra.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    ResponseEntity<ResponseDto> login(@RequestBody LoginDto dto) {

        // dto -> model
        UserModel requestModel = dto.toModel();

        LoginResponseDto loginResponseDto = authService.login(requestModel);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", loginResponseDto.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7일 동안 유지
                .sameSite("Strict") // CSRF 방지
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer %s" .formatted(loginResponseDto.accessToken()));

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(new ResponseDto("200", "success", loginResponseDto));
    }

    @PostMapping("/join")
    ResponseDto join(@RequestBody LoginDto dto) {

        // dto -> model
        UserModel requestModel = dto.toModel();

        UserModel responseModel = authService.join(requestModel);

        return new ResponseDto("200", "success", LoginDto.fromModel(responseModel));
    }
}
