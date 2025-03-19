package com.study.api.auth.dto;

import com.study.api.auth.constant.UserRole;
import com.study.api.auth.model.UserModel;

public record LoginDto(
        Long id,
        String userName,

        String userPassword,

        UserRole userRole
) {
    public UserModel toModel() {
        return UserModel.builder()
                .userName(this.userName)
                .userPassword(this.userPassword)
                .userRole(this.userRole)
                .build();

    }

    public static LoginDto fromModel(UserModel userModel) {
        return new LoginDto(
                userModel.getId(),
                userModel.getUserName(),
                userModel.getUserPassword(),
                userModel.getUserRole()
        );
    }
}
