package com.study.infra.security;

import com.study.api.auth.constant.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Slf4j
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
                .requestMatchers(HttpMethod.GET, "/token").permitAll()
                .requestMatchers(HttpMethod.GET, "/board/**").hasAnyRole(
                        UserRole.USER.name(),
                        UserRole.ADMIN.name())
                // 그 외 요청 체크
                .anyRequest().authenticated()
        );

        http.exceptionHandling(conf -> conf
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    log.info("----- ACCESS DENIED");
                })
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
