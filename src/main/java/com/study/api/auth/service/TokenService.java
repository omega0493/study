package com.study.api.auth.service;

import com.study.api.auth.dto.JwtTokenDto;
import com.study.infra.common.exception.BusinessError;
import com.study.infra.common.exception.BusinessException;
import com.study.infra.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final JwtProvider jwtProvider;

    public JwtTokenDto tokenRefresh(String refreshToken) {

        Boolean validateToken = jwtProvider.validateToken(refreshToken);

        if(!validateToken) {
            throw new BusinessException(BusinessError.REFRESH_TOKEN_INVALID);
        }

        String userId = jwtProvider.getUsernameFromToken(refreshToken);

        String accessToken = jwtProvider.generateAccessToken(userId);

        return new JwtTokenDto(refreshToken, accessToken);
    }
}
