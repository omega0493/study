package com.study.api.auth.model;

import com.study.api.auth.constant.UserRole;
import com.study.entity.user.User;
import com.study.infra.aop.UserIdAware;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModel implements UserIdAware {

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

    /**
     * 유저 역할
     */
    private UserRole userRole;

    @Builder
    UserModel(Long id, String userName, String userPassword, UserRole userRole) {
        this.id = id;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userRole = userRole;
    }

    public User toEntity() {
        return User.builder()
                .userName(this.userName)
                .userPassword(this.userPassword)
                .userRole(this.userRole)
                .build();
    }

    public static UserModel fromEntity(User user) {
        return UserModel.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .userPassword(user.getUserPassword())
                .userRole(user.getUserRole())
                .build();
    }

    @Override
    public Long getUserId() {
        return id;
    }
}
