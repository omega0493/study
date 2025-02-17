package com.study.board.model;

import com.study.auth.model.UserModel;
import com.study.entity.board.Board;
import com.study.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BoardModel {

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
    private LocalDate createDate;

    /**
     * 수정일자
     */
    private LocalDate updateDate;

    @Builder
    BoardModel(String title, String content, UserModel user, LocalDate createDate, LocalDate updateDate) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }

    public Board toEntity(BoardModel boardModel) {
        return Board.builder()
                .title(boardModel.getTitle())
                .content(boardModel.getContent())
                .user(boardModel.getUser().toEntity())
                .createDate(boardModel.getCreateDate())
                .updateDate(boardModel.getUpdateDate())
                .build();
    }

    public static BoardModel fromEntity(Board board) {
        return BoardModel.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .user(UserModel.fromEntity(board.getUser()))
                .createDate(board.getCreateDate())
                .updateDate(board.getUpdateDate())
                .build();
    }
}
