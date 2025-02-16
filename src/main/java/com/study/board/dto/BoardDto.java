package com.study.board.dto;

import com.study.board.model.BoardModel;
import com.study.entity.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BoardDto {

    private String title;

    private String content;

    private String userName;

    private String userPassword;

    private LocalDate createDate;

    private LocalDate updateDate;

    public BoardModel toModel() {
        return BoardModel.builder()
                .title(this.title)
                .content(this.content)
                .user(new User(this.userName, this.userPassword))
                .build();
    }

}
