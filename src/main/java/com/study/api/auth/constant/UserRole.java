package com.study.api.auth.constant;

import lombok.Getter;

@Getter
public enum UserRole {

    ADMIN("ROLE_ADMIN", "관리자"),
    USER("ROLE_USER", "사용자");

    private final String code;

    private final String name;

    UserRole(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
