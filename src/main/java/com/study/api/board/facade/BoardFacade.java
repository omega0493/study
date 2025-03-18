package com.study.api.board.facade;

import com.study.api.board.dto.BoardDto;
import com.study.api.board.service.BoardService;
import com.study.entity.board.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardFacade {

    private final BoardService boardService;

    public List<BoardDto> getAllBoards() {
        return boardService.getAllBoards().stream().map(BoardDto::fromEntity).toList();
    }

    // 게시글 작성
    @Transactional
    public BoardDto createBoard(BoardDto boardDto) {

        // dto -> entity
        Board board = boardDto.toEntity();

        // 회원 조회
        Board responseBoard = boardService.createBoard(board);

        return BoardDto.fromEntity(responseBoard);
    }

    // 선택한 게시글 조회
    public BoardDto getBoardById(Long id) {

        Board responseBoard = boardService.getBoardById(id);

        return BoardDto.fromEntity(responseBoard);
    }

    // 선택한 게시글 수정
    @Transactional
    public BoardDto updateBoard(Long id, BoardDto boardDto) {

        // dto -> entity
        Board board = boardDto.toEntity();

        Board responseBoard = boardService.updateBoard(id, board);


        // entity -> model
        return BoardDto.fromEntity(responseBoard);
    }

    // 선택한 게시글 삭제
    @Transactional
    public BoardDto deleteBoard(Long id) {

        Board board = boardService.deleteBoard(id);

        return BoardDto.fromEntity(board);
    }

}
