package com.study.infra.security;

import com.study.api.auth.constant.UserRole;
import com.study.api.auth.model.UserModel;
import com.study.api.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CustomAuthenticationSuccessHandler.class)
class CustomAuthenticationSuccessHandlerTest {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @MockitoBean
    private AuthService authService;

    @Mock
    private Authentication authentication;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    void onAuthenticationSuccess() throws IOException {

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
        successHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        Authentication contextAuth = SecurityContextHolder.getContext().getAuthentication();

        assertThat(contextAuth)
                .isNotNull()
                .returns("foo", Authentication::getPrincipal)
                .returns("bar", Authentication::getCredentials)
                .returns(model, Authentication::getDetails)
                .extracting(Authentication::getAuthorities, as(InstanceOfAssertFactories.LIST))
                .containsExactly(new SimpleGrantedAuthority(UserRole.USER.getCode()));
    }

}
