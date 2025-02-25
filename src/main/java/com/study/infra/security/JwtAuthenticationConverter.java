package com.study.infra.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JwtAuthenticationConverter implements AuthenticationConverter {

    private static final Pattern AUTHORIZATION_HEADER_PATTERN = Pattern.compile("^Bearer (.+)");

    @Override
    public Authentication convert(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 인증 절차를 거치지 않고 다음 필터로 넘긴다.
        if (!StringUtils.hasText(header) || !AUTHORIZATION_HEADER_PATTERN.matcher(header).matches()) {
            return null;
        }

        // Bearer token 검증 후 user name 조회
        Matcher matcher = AUTHORIZATION_HEADER_PATTERN.matcher(header);
        if (!matcher.find()) {
            return null;
        }

        String jwtToken = matcher.group(1);

        return new UsernamePasswordAuthenticationToken(null, jwtToken);
    }

}
