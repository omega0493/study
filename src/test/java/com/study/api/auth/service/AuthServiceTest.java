package com.study.api.auth.service;

import com.study.api.auth.dto.LoginResponseDto;
import com.study.api.auth.model.UserModel;
import com.study.api.auth.repository.UserRepository;
import com.study.entity.user.User;
import com.study.infra.common.exception.BusinessException;
import com.study.infra.security.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(classes = AuthService.class)
class AuthServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder encoder;

    @MockitoBean
    private JwtProvider jwtProvider;

    @Autowired
    private AuthService sut;

    @Test
    void successToLogin() {

        // given
        UserModel model = UserModel.builder()
                .userName("foo")
                .userPassword("bar")
                .build();

        LoginResponseDto loginResponseDto = LoginResponseDto.fromModel(model, "", "");

        when(userRepository.findByUserName("foo"))
                .then(invocation -> {
                    User user = User.builder()
                            .userName("foo")
                            .userPassword("$2a$10$u6g7CRVI8PAZa7sz7xNkjOi2F6Jwpf8d08vAX5W0eX8T.RxUhVFy2")
                            .build();
                    ReflectionTestUtils.setField(user, "id", 1L);

                    return Optional.of(user);
                });
        when(encoder.matches(eq(model.getUserPassword()), any())).thenReturn(true);
        when(jwtProvider.generateAccessToken(any())).thenReturn("access_token");
        when(jwtProvider.generateRefreshToken(any())).thenReturn("refresh_token");

        // when
        LoginResponseDto result = sut.login(model);

        // then
        assertThat(result)
                .isNotNull()
                .returns(loginResponseDto.userName(), LoginResponseDto::userName);
//                .returns(model.getUserName(), UserModel::getUserName);
    }

    @Test
    void failToLogin() {

        // given
        UserModel model = UserModel.builder()
                .userName("foo")
                .userPassword("bar")
                .build();
        when(userRepository.findByUserName("foo"))
                .thenReturn(Optional.empty());

        // then
        assertThatException()
                .isThrownBy(() -> sut.login(model))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void successToJoin() {

        // given
        UserModel model = UserModel.builder()
                .userName("foo")
                .userPassword("bar")
                .build();
        when(userRepository.findByUserName("foo"))
                .thenReturn(Optional.empty());
        when(userRepository.save(any()))
                .then(it -> {
                    User user = User.builder()
                            .userName("foo")
                            .build();

                    ReflectionTestUtils.setField(user, "id", 1L);

                    return user;
                });

        // when
        UserModel result = sut.join(model);

        // then
        assertThat(result)
                .isNotNull()
                .matches(it -> it.getId() != null)
                .returns(model.getUserName(), UserModel::getUserName);
    }

    @Test
    void failToJoin() {

        // given
        UserModel model = UserModel.builder()
                .userName("foo")
                .userPassword("bar")
                .build();
        when(userRepository.findByUserName("foo"))
                .thenReturn(Optional.of(User.builder()
                        .userName("foo")
                        .userPassword("bar")
                        .build()));

        // then
        assertThatException()
                .isThrownBy(() -> sut.join(model))
                .isInstanceOf(BusinessException.class);
    }

}
