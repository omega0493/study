package com.study.api.board.controller;

import com.study.api.auth.model.UserModel;
import com.study.api.board.dto.BoardDto;
import com.study.api.board.facade.BoardFacade;
import com.study.infra.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardFacade boardFacade;

    @GetMapping("/all")
    List<BoardDto> getAllBoards(UserModel userModel) {

        return boardFacade.getAllBoards();
    }

    @PostMapping
    BoardDto createBoard(@RequestBody BoardDto boardDto, UserModel userModel) {

        return boardFacade.createBoard(boardDto);
    }

    @GetMapping("/{id}")
    BoardDto getBoardById(@PathVariable Long id, UserModel userModel) {

        return boardFacade.getBoardById(id);
    }

    @PutMapping("/{id}")
    BoardDto updateBoard(@PathVariable Long id, @RequestBody BoardDto boardDto, @AuthUser UserModel userModel) {

        return boardFacade.updateBoard(id, boardDto);
    }

    @DeleteMapping("/{id}")
    BoardDto deleteBoard(@PathVariable Long id, UserModel userModel) {

        return boardFacade.deleteBoard(id);
    }
}
