package com.study.api.board.repository;

import com.study.api.auth.constant.UserRole;
import com.study.api.auth.model.UserModel;
import com.study.api.auth.repository.UserRepository;
import com.study.api.board.model.BoardModel;
import com.study.entity.board.Board;
import com.study.entity.user.User;
import com.study.infra.common.configuration.JpaConfiguration;
import com.study.test.config.TestAuditorAwareImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import({JpaConfiguration.class, TestAuditorAwareImpl.class})
@DataJpaTest
class BoardRepositoryTest {

    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
            .withUsername("admin")
            .withPassword("foobar")
            .withDatabaseName("study");

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private EntityManager entityManager;

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);
        dynamicPropertyRegistry.add("spring.jpa.hibernate.ddl-auto", () -> "create");

    }

    @BeforeAll
    static void beforeAll() {
        mySQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        mySQLContainer.stop();
    }

    @Test
    void findAllWithUserTest() {

        //given
        User user = User.builder()
                .userName("foo")
                .userPassword("$2a$10$u6g7CRVI8PAZa7sz7xNkjOi2F6Jwpf8d08vAX5W0eX8T.RxUhVFy2")
                .userRole(UserRole.USER)
                .build();

        entityManager.persist(user);

        Board board = Board.builder()
                .title("Spring Security 설정하기")
                .content("Spring Security를 활용한 인증과 인가 설정 방법을 정리합니다.")
                .user(user)
                .build();

        entityManager.persist(board);

        //when
        List<Board> boards = boardRepository.findAllWithUser();

        //then
        assertThat(boards)
                .isNotNull()
                .hasSize(1)
                .containsExactly(board);
    }
}