package com.study.api.auth.repository;

import com.study.api.auth.constant.UserRole;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import({JpaConfiguration.class, TestAuditorAwareImpl.class})
@DataJpaTest
class UserRepositoryTest {

    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
            .withUsername("admin")
            .withPassword("foobar")
            .withDatabaseName("study");

    @Autowired
    private UserRepository userRepository;

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
    void findByUserNameTest() {

        //given
        User user = User.builder()
                .userName("foo")
                .userPassword("$2a$10$u6g7CRVI8PAZa7sz7xNkjOi2F6Jwpf8d08vAX5W0eX8T.RxUhVFy2")
                .userRole(UserRole.USER)
                .build();
        entityManager.persist(user);

        //when
        Optional<User> maybeUser = userRepository.findByUserName(user.getUserName());

        //then
        assertThat(maybeUser)
                .isNotNull()
                .isPresent()
                .get()
                .returns(user.getUserName(), User::getUserName);
    }

}
