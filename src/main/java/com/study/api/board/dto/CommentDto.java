package com.study.api.board.dto;

import com.study.api.auth.model.UserModel;
import com.study.api.board.model.BoardModel;
import com.study.api.board.model.CommentModel;

import java.time.LocalDateTime;

public record CommentDto(
        Long id,

        String content,

        String userName,

        LocalDateTime createDate,

        LocalDateTime updateDate
) {
    public BoardModel toModel() {
        return BoardModel.builder()
                .content(this.content)
                .user(UserModel.builder()
                        .userName(this.userName)
                        .build())
                .build();
    }

    public static CommentDto fromModel(CommentModel commentModel) {
        return new CommentDto(
                commentModel.getId(),
                commentModel.getContent(),
                commentModel.getUser().getUserName(),
                commentModel.getCreateDate(),
                commentModel.getUpdateDate()
        );
    }

}
