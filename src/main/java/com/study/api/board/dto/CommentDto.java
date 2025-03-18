package com.study.api.board.dto;

import com.study.entity.board.Comment;

import java.time.LocalDateTime;

public record CommentDto(
        Long id,

        String content,

        String userName,

        LocalDateTime createDate,

        LocalDateTime updateDate
) {
//    public BoardModel toModel() {
//        return BoardModel.builder()
//                .content(this.content)
//                .user(UserModel.builder()
//                        .userName(this.userName)
//                        .build())
//                .build();
//    }

    public static CommentDto fromModel(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getUserName(),
                comment.getCreatedAt(),
                comment.getLastModifiedAt()
        );
    }

}
