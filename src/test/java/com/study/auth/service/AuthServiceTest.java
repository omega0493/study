package com.study.auth.service;

import com.study.auth.model.UserModel;
import com.study.entity.user.User;
import com.study.entity.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthServiceTest {

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private AuthService sut;

    @Test
    void loginTest() {

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
    void joinTest() {

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
        UserModel result = sut.join(model);

        // then
        assertThat(result).isNotNull();
        assertThat(model.getUserName()).isEqualTo(result.getUserName());
        assertThat(model.getUserPassword()).isEqualTo(result.getUserPassword());
    }

}
