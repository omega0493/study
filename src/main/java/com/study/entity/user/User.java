package com.study.entity.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자
 */
@Getter
@NoArgsConstructor
@Entity
@Table(name = "USER")
public class User {

    /**
     * 사용자 아이디
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", nullable = false, updatable = false)
    private Long id;

    /**
     * 사용자 이름
     */
    @Column(name = "USER_NAME", nullable = false, unique = true)
    private String userName;

    /**
     * 사용자 비밀번호
     */
    @Column(name = "USER_PASSWORD", nullable = false)
    private String userPassword;

    @Builder
    User(String userName, String userPassword) {

        this.userName = userName;
        this.userPassword = userPassword;
    }

}
