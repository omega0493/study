package com.study.api.auth.service;

import com.study.api.auth.dto.LoginResponseDto;
import com.study.api.auth.model.UserModel;
import com.study.api.auth.service.AuthService;
import com.study.infra.common.exception.BusinessException;
import com.study.entity.user.User;
import com.study.api.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private AuthService sut;

    @Test
    void successToLogin() {

        // given
        UserModel model = UserModel.builder()
                .userName("foo")
                .userPassword("$2a$10$u6g7CRVI8PAZa7sz7xNkjOi2F6Jwpf8d08vAX5W0eX8T.RxUhVFy2")
                .build();

        LoginResponseDto loginResponseDto = LoginResponseDto.fromModel(model, "", "");

        when(userRepository.findByUserName("foo"))
                .thenReturn(Optional.of(User.builder()
                        .userName("foo")
                        .userPassword("$2a$10$u6g7CRVI8PAZa7sz7xNkjOi2F6Jwpf8d08vAX5W0eX8T.RxUhVFy2")
                        .build()));

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
