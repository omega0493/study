package com.study.api.auth.dto;


public record JwtTokenDto (
        String refreshToken ,

        String accessToken

) {
}
