package com.study.auth.dto;

import com.study.auth.model.UserModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {

    private String userName;

    private String userPassword;

    public UserModel toModel() {
        return UserModel.builder()
                .userName(this.userName)
                .userPassword(this.userPassword)
                .build();

    }
}
