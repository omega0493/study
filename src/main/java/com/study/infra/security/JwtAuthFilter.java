package com.study.infra.security;

import com.study.api.auth.model.UserModel;
import com.study.api.auth.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Pattern AUTHORIZATION_HEADER_PATTERN = Pattern.compile("^Bearer (.+)");

    private final AuthService authService;

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request
            , HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 인증 절차를 거치지 않고 다음 필터로 넘긴다.
        if (!StringUtils.hasText(header) || !AUTHORIZATION_HEADER_PATTERN.matcher(header).matches()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer token 검증 후 user name 조회
        String jwtToken = header.substring(7);

        String userId = jwtProvider.getUsernameFromToken(jwtToken);

        UserModel savedModel = authService.getUserById(Long.valueOf(userId));

        if (savedModel == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                savedModel.getUserName(), savedModel.getUserPassword(), Collections.emptyList());
        auth.setDetails(savedModel);

        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

}
