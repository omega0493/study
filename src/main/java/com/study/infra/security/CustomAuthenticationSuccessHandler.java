package com.study.infra.security;

import com.study.api.auth.model.UserModel;
import com.study.api.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String username = authentication.getName();

        UserModel userById = authService.getUserById(Long.valueOf(username));

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userById.getUserRole().getCode()));

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userById.getUserName(), userById.getUserPassword(), authorities);

        SecurityContextHolder.getContext().setAuthentication(auth);
        auth.setDetails(userById);
    }

}
