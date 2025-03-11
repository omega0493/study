package com.study.entity.board;

import com.study.entity.base.AbstractAuditableEntity;
import com.study.entity.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "COMMENT")
public class Comment extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID", referencedColumnName = "BOARD_ID", updatable = false)
    private Board board;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Builder
    @SuppressWarnings("unused")
    Comment(String title, String content, User user, Board board, LocalDateTime createDate, LocalDateTime updateDate) {
        this.content = content;
        this.user = user;
        this.board = board;
    }

}
