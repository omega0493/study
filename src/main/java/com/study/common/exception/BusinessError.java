package com.study.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum BusinessError {

    // User ---------------------------------------------------------------------------------------------------

    NO_REGISTERED_USER("user:login:no_registered_user", "존재하지 않는 유저 입니다."),
    REGISTERED_USER("user:join:registered_user", "이미 존재하는 유저 입니다."),
    ;

    BusinessError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;

    private final String message;
}
