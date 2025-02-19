package com.study.api.auth.model;

import com.study.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModel {

    /**
     * 유저 번호
     */
    private Long id;

    /**
     * 유저 이름
     */
    private String userName;

    /**
     * 유저 비밀번호
     */
    private String userPassword;

    @Builder
    UserModel(Long id, String userName, String userPassword) {
        this.id = id;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public User toEntity() {
        return User.builder()
                .userName(this.userName)
                .userPassword(this.userPassword)
                .build();
    }

    public static UserModel fromEntity(User user) {
        return UserModel.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .userPassword(user.getUserPassword())
                .build();
    }
}
