package com.study.auth.controller;

import com.study.auth.dto.LoginDto;
import com.study.auth.dto.ResponseDto;
import com.study.auth.model.UserModel;
import com.study.auth.service.AuthService;
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

    @PostMapping
    ResponseDto login(@RequestBody LoginDto dto) {

        // dto -> model
        UserModel requestModel = dto.toModel();

        UserModel responseModel = authService.login(requestModel);

        return new ResponseDto("200", "success", responseModel);
    }
}
