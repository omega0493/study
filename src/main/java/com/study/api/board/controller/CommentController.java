package com.study.api.board.controller;

import com.study.api.board.dto.BoardDto;
import com.study.api.board.dto.CommentDto;
import com.study.api.board.model.CommentModel;
import com.study.api.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{id}")
    CommentDto getCommentById(@PathVariable Long id) {

        CommentModel responseModel = commentService.getCommentById(id);

        return CommentDto.fromModel(responseModel);
    }




}
