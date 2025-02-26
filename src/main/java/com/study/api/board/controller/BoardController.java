package com.study.api.board.controller;

import com.study.api.auth.model.UserModel;
import com.study.infra.common.dto.ResponseDto;
import com.study.api.board.dto.BoardDto;
import com.study.api.board.model.BoardModel;
import com.study.api.board.service.BoardService;
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
    ResponseDto createBoard(@RequestBody BoardDto boardDto) {

        // dto -> model
        BoardModel requestModel = boardDto.toModel();

        BoardModel responseModel = boardService.createBoard(requestModel);

        return new ResponseDto("200", "success", BoardDto.fromModel(responseModel));
    }

    @GetMapping("/{id}")
    ResponseDto getBoardById(@PathVariable Long id) {

        BoardModel responseModel = boardService.getBoardById(id);

        return new ResponseDto("200", "success", BoardDto.fromModel(responseModel));
    }

    @PutMapping("/{id}")
    ResponseDto updateBoard(@PathVariable Long id, @RequestBody BoardDto boardDto) {

        // dto -> model
        BoardModel requestModel = boardDto.toModel();

        BoardModel responseModel = boardService.updateBoard(id, requestModel);

        return new ResponseDto("200", "success", BoardDto.fromModel(responseModel));
    }

    @PutMapping("/{id}/delete")
    ResponseDto deleteBoard(@PathVariable Long id, @RequestBody BoardDto boardDto) {

        // dto -> model
        BoardModel requestModel = boardDto.toModel();

        BoardModel responseModel = boardService.deleteModel(id, requestModel);

        return new ResponseDto("200", "success", BoardDto.fromModel(responseModel));
    }
}
