package com.study.auth.service;

import com.study.auth.model.UserModel;
import com.study.common.exception.BusinessException;
import com.study.entity.user.User;
import com.study.entity.user.repository.UserRepository;
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
                .userPassword("bar")
                .build();
        when(userRepository.findByUserName("foo"))
                .thenReturn(Optional.of(User.builder()
                        .userName("foo")
                        .userPassword("bar")
                        .build()));

        // when
        UserModel result = sut.login(model);

        // then
        assertThat(result)
                .isNotNull()
                .returns(model.getUserName(), UserModel::getUserName);
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
