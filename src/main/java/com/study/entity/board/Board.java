package com.study.entity.board;

import com.study.api.board.model.BoardModel;
import com.study.entity.base.AbstractAuditableEntity;
import com.study.entity.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 *  게시글
 */
@Getter
@NoArgsConstructor
@Entity
@Table(name = "BOARD")
public class Board extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID", nullable = false, updatable = false)
    private Long id;

    /**
     * 제목
     */
    @Column(name = "TITLE", nullable = false)
    private String title;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", updatable = false)
    private User user;

    /**
     * 내용
     */
    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Builder
    @SuppressWarnings("unused")
    Board(String title, String content, User user, LocalDateTime createDate, LocalDateTime updateDate) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void edit(BoardModel board) {
        this.title = board.getTitle();
        this.content = board.getContent();
//        this.user = board.getUser();
    }
}
