package com.study.infra.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public static AuthenticationFilter authenticationFilter(List<AuthenticationProvider> providers) {
        ProviderManager manager = new ProviderManager(providers);
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        return new AuthenticationFilter(manager, converter);
    }

    @Bean
    public SecurityFilterChain config(
            HttpSecurity http,
            AuthenticationFilter authenticationFilter,
            AuthenticationSuccessHandler authenticationSuccessHandler
    ) throws Exception {
        // http request 인증 설정
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/join").permitAll()
                // 그 외 요청 체크
                .anyRequest().authenticated()
        );

        // form login disable
        http.formLogin(AbstractHttpConfigurer::disable);

        // logout disable
        http.logout(AbstractHttpConfigurer::disable);

        // csrf disable
        http.csrf(AbstractHttpConfigurer::disable);

        // session management
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 미사용
        );

        // before filter
        authenticationFilter.setSuccessHandler(authenticationSuccessHandler);
        authenticationFilter.setFailureHandler(new CustomAuthenticationFailureHandler());
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
