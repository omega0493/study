package com.study.api.board.service;

import com.study.api.board.repository.CommentRepository;
import com.study.entity.board.Comment;
import com.study.infra.common.exception.BusinessError;
import com.study.infra.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment getCommentById(Long id) {

        return commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BusinessError.NO_REGISTERED_COMMENT));
    }
}
