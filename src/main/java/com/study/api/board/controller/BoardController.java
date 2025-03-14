package com.study.api.board.controller;

import com.study.api.auth.model.UserModel;
import com.study.api.board.dto.BoardDto;
import com.study.api.board.model.BoardModel;
import com.study.api.board.service.BoardService;
import com.study.infra.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/all")
    List<BoardDto> getAllBoards(UserModel userModel) {

        List<BoardModel> responseModel = boardService.getAllBoards();

        // model -> dto
        List<BoardDto> boardDto = new ArrayList<>();

        for (BoardModel model : responseModel) {
            BoardDto dto = BoardDto.fromModel(model);

            boardDto.add(dto);
        }

        return boardDto;
    }

    @PostMapping
    BoardDto createBoard(@RequestBody BoardDto boardDto, UserModel userModel) {

        // dto -> model
        BoardModel requestModel = boardDto.toModel();

        BoardModel responseModel = boardService.createBoard(requestModel);

        return BoardDto.fromModel(responseModel);
    }

    @GetMapping("/{id}")
    BoardDto getBoardById(@PathVariable Long id, UserModel userModel) {

        BoardModel responseModel = boardService.getBoardById(id);

        return BoardDto.fromModel(responseModel);
    }

    @PutMapping("/{id}")
    BoardDto updateBoard(@PathVariable Long id, @RequestBody BoardDto boardDto, @AuthUser UserModel userModel) {

        // dto -> model
        BoardModel requestModel = boardDto.toModel();

        BoardModel responseModel = boardService.updateBoard(id, requestModel);

        return BoardDto.fromModel(responseModel);
    }

    @DeleteMapping("/{id}")
    BoardDto deleteBoard(@PathVariable Long id, UserModel userModel) {

        BoardModel responseModel = boardService.deleteBoard(id);

        return BoardDto.fromModel(responseModel);
    }
}
