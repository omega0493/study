package com.study.api.auth.controller;

import com.study.api.auth.controller.AuthController;
import com.study.api.auth.dto.LoginDto;
import com.study.api.auth.model.UserModel;
import com.study.api.auth.service.AuthService;
import com.study.test.config.TestSecurityConfig;
import com.study.test.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @MockitoBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginTest() throws Exception {

        // given
        LoginDto dto = new LoginDto(1L, "foo", "bar");

        when(authService.login(any()))
                .thenReturn(UserModel.builder()
                        .id(1L)
                        .userName("foo")
                        .userPassword("bar")
                        .build());

        mockMvc
                // when
                .perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.toJson(dto))
                )
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("200")))
                .andExpect(jsonPath("$.message", equalTo("success")))
                .andExpect(jsonPath("$.data.id", equalTo(1)))
                .andExpect(jsonPath("$.data.userName", equalTo("foo")))
                .andExpect(jsonPath("$.data.userPassword", equalTo("bar")));
    }

    @Test
    void joinTest() throws Exception {
        // given
        LoginDto dto = new LoginDto(1L, "foo", "bar");

        when(authService.join(any()))
                .thenReturn(UserModel.builder()
                        .id(1L)
                        .userName("foo")
                        .userPassword("bar")
                        .build());

        mockMvc
                // when
                .perform(post("/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.toJson(dto))
                )
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("200")))
                .andExpect(jsonPath("$.message", equalTo("success")))
                .andExpect(jsonPath("$.data.id", equalTo(1)))
                .andExpect(jsonPath("$.data.userName", equalTo("foo")))
                .andExpect(jsonPath("$.data.userPassword", equalTo("bar")));
    }

}