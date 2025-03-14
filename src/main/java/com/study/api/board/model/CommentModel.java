package com.study.api.board.model;

import com.study.api.auth.model.UserModel;
import com.study.entity.board.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentModel {

    /**
     * 댓글 번호
     */
    private Long id;

    /**
     * 내용
     */
    private String content;

    private UserModel user;

    private BoardModel board;

    /**
     * 작성일자
     */
    private LocalDateTime createDate;

    /**
     * 수정일자
     */
    private LocalDateTime updateDate;

    @Builder
    CommentModel(Long id, String content, UserModel user, BoardModel board, LocalDateTime createDate, LocalDateTime updateDate) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.board = board;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public Comment toEntity() {
        return Comment.builder()
                .content(this.getContent())
                .user(this.getUser().toEntity())
                .board(this.getBoard().toEntity())
                .createDate(this.getCreateDate())
                .updateDate(this.getUpdateDate())
                .build();
    }

    public static CommentModel fromEntity(Comment comment) {
        return CommentModel.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(UserModel.fromEntity(comment.getUser()))
                .board(BoardModel.fromEntity(comment.getBoard()))
                .build();
    }
}
