package com.study.infra.security;

import com.study.api.auth.constant.UserRole;
import com.study.api.auth.model.UserModel;
import com.study.api.auth.repository.UserRepository;
import com.study.api.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CustomAuthenticationSuccessHandler.class)
class CustomAuthenticationSuccessHandlerTest {

    @MockitoBean
    private AuthService authService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserRepository userRepository;

    @Test
    void onAuthenticationSuccess() {

        // given
        UserModel model = UserModel.builder()
                .id(1L)
                .userName("foo")
                .userPassword("bar")
                .userRole(UserRole.USER)
                .build();

        when(authentication.getName()).thenReturn("1");
        when(authService.getUserById(1L)).thenReturn(model);

        // when
        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(model.getUserRole().getCode()));


        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                model.getUserName(), model.getUserPassword(), authorities);

        auth.setDetails(model);

        SecurityContextHolder.getContext().setAuthentication(auth);

        // then
        assertThat(authorities)
                .containsExactly(new SimpleGrantedAuthority("ROLE_USER"));

        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isEqualTo(auth);

        assertThat(auth.getPrincipal())
                .isEqualTo("foo");

        assertThat(auth.getCredentials())
                .isEqualTo("bar");

        assertThat(auth.getDetails())
                .isEqualTo(model);

    }
}