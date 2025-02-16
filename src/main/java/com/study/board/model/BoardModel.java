package com.study.board.model;

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

    private User user;

    /**
     * 작성일자
     */
    private LocalDate createDate;

    /**
     * 수정일자
     */
    private LocalDate updateDate;

    @Builder
    BoardModel(String title, String content, User user, LocalDate createDate, LocalDate updateDate) {
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
                .user(boardModel.getUser())
                .createDate(boardModel.getCreateDate())
                .updateDate(boardModel.getUpdateDate())
                .build();
    }

    public static BoardModel fromEntity(Board board) {
        return BoardModel.builder()
                .title(board.getTitle())
                .content(board.getContent())
                .user(board.getUser())
                .createDate(board.getCreateDate())
                .updateDate(board.getUpdateDate())
                .build();
    }
}
