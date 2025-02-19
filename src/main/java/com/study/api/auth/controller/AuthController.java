package com.study.api.auth.controller;

import com.study.api.auth.dto.LoginDto;
import com.study.api.auth.model.UserModel;
import com.study.api.auth.service.AuthService;
import com.study.infra.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
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
    ResponseDto login(@RequestBody LoginDto dto) {

        // dto -> model
        UserModel requestModel = dto.toModel();

        UserModel responseModel = authService.login(requestModel);

        return new ResponseDto("200", "success", LoginDto.fromModel(responseModel));
    }

    @PostMapping("/join")
    ResponseDto join(@RequestBody LoginDto dto) {

        // dto -> model
        UserModel requestModel = dto.toModel();

        UserModel responseModel = authService.join(requestModel);

        return new ResponseDto("200", "success", LoginDto.fromModel(responseModel));
    }
}
