package com.study.board.dto;

import com.study.auth.model.UserModel;
import com.study.board.model.BoardModel;

import java.time.LocalDateTime;

public record BoardDto(
        Long id,
        String title,

        String content,

        String userName,

        String userPassword,

        LocalDateTime createDate,

        LocalDateTime updateDate
) {
    public BoardModel toModel() {
        return BoardModel.builder()
                .title(this.title)
                .content(this.content)
                .user(UserModel.builder()
                        .userName(this.userName)
                        .userPassword(this.userPassword)
                        .build())
                .build();
    }

    public static BoardDto fromModel(BoardModel boardModel) {
        return new BoardDto(
                boardModel.getId(),
                boardModel.getTitle(),
                boardModel.getContent(),
                boardModel.getUser().getUserName(),
                boardModel.getUser().getUserPassword(),
                boardModel.getCreateDate(),
                boardModel.getUpdateDate()
        );
    }

}
