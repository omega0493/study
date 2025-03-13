package com.study.api.board.model;

import com.study.api.auth.model.UserModel;
import com.study.entity.board.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardModel {

    /**
     * 게시물 번호
     */
    private Long id;

    /**
     * 제목
     */
    private String title;

    /**
     * 내용
     */
    private String content;

    private UserModel user;

    /**
     * 작성일자
     */
    private LocalDateTime createDate;

    /**
     * 수정일자
     */
    private LocalDateTime updateDate;

    @Builder
    BoardModel(Long id, String title, String content, UserModel user, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public Board toEntity() {
        return Board.builder()
                .title(this.getTitle())
                .content(this.getContent())
                .user(this.getUser().toEntity())
                .createDate(this.getCreateDate())
                .updateDate(this.getUpdateDate())
                .build();
    }

    public static BoardModel fromEntity(Board board) {
        return BoardModel.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .user(UserModel.fromEntity(board.getUser()))
                .build();
    }
}
