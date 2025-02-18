package com.study.entity.board;

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
public class Board {

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
    @JoinColumn(name = "USER_NAME", referencedColumnName = "USER_NAME", insertable = false, updatable = false)
    private User user;

    /**
     * 내용
     */
    @Column(name = "CONTENT", nullable = false)
    private String content;

    /**
     * 작성 날짜
     */
    @CreatedDate
    @Column(name = "CREATE_DATE", nullable = false, updatable = false)
    private LocalDateTime createDate;

    /**
     * 수정 날짜
     */
    @LastModifiedDate
    @Column(name = "UPDATE_DATE", nullable = false)
    private LocalDateTime updateDate;

    @Builder
    @SuppressWarnings("unused")
    Board(String title, String content, User user, LocalDateTime createDate, LocalDateTime updateDate) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public void edit(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
        this.user = board.getUser();
        this.updateDate = board.getUpdateDate();
    }
}
