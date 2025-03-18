package com.study.api.board.dto;

import com.study.entity.board.Board;
import com.study.entity.user.User;

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
    public Board toEntity() {
        return Board.builder()
                .title(this.title)
                .content(this.content)
                .user(User.builder()
                        .userName(this.userName)
                        .userPassword(this.userPassword)
                        .build())
                .build();
    }

    public static BoardDto fromEntity(Board board) {
        return new BoardDto(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getUser().getUserName(),
                board.getUser().getUserPassword(),
                board.getCreatedAt(),
                board.getLastModifiedAt()
        );
    }

}
