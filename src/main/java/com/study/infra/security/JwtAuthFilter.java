package com.study.infra.security;

import com.study.api.auth.model.UserModel;
import com.study.api.auth.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Pattern AUTHORIZATION_HEADER_PATTERN = Pattern.compile("^Bearer (.+)");

    private final AuthService authService;

    private final PasswordEncoder passwordEncoder;

//    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request
            , HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        // 인증 절차를 거치지 않고 다음 필터로 넘긴다.
        if (!StringUtils.hasText(header) || !AUTHORIZATION_HEADER_PATTERN.matcher(header).matches()) {
            filterChain.doFilter(request, response);
            return;
        }

        Matcher matcher = AUTHORIZATION_HEADER_PATTERN.matcher(header);
        matcher.find();
        String token = matcher.group(1);

        String[] fragments = token.split("/");
        String userName = fragments[0];
        String password = fragments[1];

        UserModel requestedModel = UserModel.builder()
                .userName(userName)
                .userPassword(password)
                .build();

//        // Bearer token 검증 후 user name 조회
//        if(token != null && !token.isEmpty()) {
//            String jwtToken = token.substring(7);
//
//            username = jwtProvider.getUsernameFromToken(jwtToken);
//        }

        UserModel savedModel = authService.login(requestedModel);

        if (savedModel == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        // FIXME: passwordEncoder.matches()
        if (!savedModel.getUserPassword().equals(requestedModel.getUserPassword())) {
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                savedModel.getUserName(), savedModel.getUserPassword(), Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);

//        // token 검증 완료 후 SecurityContextHolder 내 인증 정보가 없는 경우 저장
//        if(username != null && !username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
//            // Spring Security Context Holder 인증 정보 set
//            SecurityContextHolder.getContext().setAuthentication(getUserAuth(username));
//        }

        filterChain.doFilter(request, response);
    }

    /**
     * token의 사용자 idx를 이용하여 사용자 정보 조회하고, UsernamePasswordAuthenticationToken 생성
     *
     * @param username 사용자 idx
     * @return 사용자 UsernamePasswordAuthenticationToken
     */
//    private UsernamePasswordAuthenticationToken getUserAuth(String username) {
//        var userInfo = userGetService.getUserById(Long.parseLong(username));
//
//        return new UsernamePasswordAuthenticationToken(userInfo.id(),
//                userInfo.password(),
//                Collections.singleton(new SimpleGrantedAuthority(userInfo.roleName().name()))
//        );
//    }
}
