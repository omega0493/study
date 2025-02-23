package com.study.api.auth.dto;

import com.study.api.auth.model.UserModel;

public record LoginResponseDto(
        Long id,
        String userName,

        String userPassword,

        String accessToken,

        String refreshToken
) {
    public UserModel toModel() {
        return UserModel.builder()
                .userName(this.userName)
                .userPassword(this.userPassword)
                .build();

    }

    public static LoginResponseDto fromModel(UserModel userModel, String accessToken, String refreshToken) {
        return new LoginResponseDto(
                userModel.getId(),
                userModel.getUserName(),
                userModel.getUserPassword(),
                accessToken,
                refreshToken
        );
    }
}
