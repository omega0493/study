package com.study.infra.common.exception;

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
    PASSWORD_MISMATCH("user:find:password_mismatch", "비밀번호가 일치하지 않습니다."),
    INCORRECT_CREDENTIALS("user:auth:incorrect_credentials", "잘못된 인증 정보입니다."),

    // Token
    REFRESH_TOKEN_INVALID("token:refresh_token_invalid", "Refresh Token이 만료되었거나 정상적인 Token이 아닙니다."),

    // Board --------------------------------------------------------------------------------------------------
    NO_REGISTERED_BOARD("board:find:no_registered_board", "존재하지 않는 게시글 입니다."),

    // Comment --------------------------------------------------------------------------------------------------
    NO_REGISTERED_COMMENT("board:find:no_registered_comment", "존재하지 않는 댓글 입니다."),
    ;

    BusinessError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private final String code;

    private final String message;
}
