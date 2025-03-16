package com.study.api.board.controller;

import com.study.api.auth.constant.UserRole;
import com.study.api.auth.model.UserModel;
import com.study.api.board.dto.BoardDto;
import com.study.api.board.model.BoardModel;
import com.study.api.board.service.BoardService;
import com.study.test.config.TestSecurityConfig;
import com.study.test.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Import(TestSecurityConfig.class)
@WebMvcTest(BoardController.class)
class BoardControllerTest {

    @MockitoBean
    private BoardService boardService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllBoardsTest() throws Exception {

        //given
        BoardDto dto = new BoardDto(
                1L,
                "Spring Security 설정하기",
                "Spring Security를 활용한 인증과 인가 설정 방법을 정리.",
                "foo",
                "bar",
                null,
                null
        );

        BoardModel boardModel = BoardModel.builder()
                .id(1L)
                .title("Spring Security 설정하기")
                .content("Spring Security를 활용한 인증과 인가 설정 방법을 정리합니다.")
                .user(UserModel.builder()
                        .id(1L)
                        .userName("foo")
                        .userPassword("bar")
                        .build())
                .createDate(null)
                .updateDate(null)
                .build();

        List<BoardModel> boardModelList = new ArrayList<>();

        boardModelList.add(boardModel);

        when(boardService.getAllBoards())
                .thenReturn(boardModelList);

        mockMvc
                //when
                .perform(get("/board/all")
                        .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("200")))
                .andExpect(jsonPath("$.message", equalTo("요청이 성공적으로 처리되었습니다.")))
                .andExpect(jsonPath("$.data[0].id", equalTo(1)))
                .andExpect(jsonPath("$.data[0].title", equalTo("Spring Security 설정하기")))
                .andExpect(jsonPath("$.data[0].content", equalTo("Spring Security를 활용한 인증과 인가 설정 방법을 정리합니다.")))
                .andExpect(jsonPath("$.data[0].userName", equalTo("foo")))
                .andExpect(jsonPath("$.data[0].userPassword").exists())
                .andExpect(jsonPath("$.data[0].createDate").isEmpty())
                .andExpect(jsonPath("$.data[0].updateDate").isEmpty());

    }

    @Test
    void createBoardTest() throws Exception {

        //given
        BoardDto dto = new BoardDto(
                1L,
                "Spring Security 설정하기",
                "Spring Security를 활용한 인증과 인가 설정 방법을 정리.",
                "foo",
                "bar",
                null,
                null
        );

        BoardModel boardModel = BoardModel.builder()
                .id(1L)
                .title("Spring Security 설정하기")
                .content("Spring Security를 활용한 인증과 인가 설정 방법을 정리합니다.")
                .user(UserModel.builder()
                        .id(1L)
                        .userName("foo")
                        .userPassword("bar")
                        .build())
                .createDate(null)
                .updateDate(null)
                .build();

        when(boardService.createBoard(any()))
                .thenReturn(boardModel);

        mockMvc
                //when
                .perform(post("/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.toJson(dto))
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("200")))
                .andExpect(jsonPath("$.message", equalTo("요청이 성공적으로 처리되었습니다.")))
                .andExpect(jsonPath("$.data.id", equalTo(1)))
                .andExpect(jsonPath("$.data.title", equalTo("Spring Security 설정하기")))
                .andExpect(jsonPath("$.data.content", equalTo("Spring Security를 활용한 인증과 인가 설정 방법을 정리합니다.")))
                .andExpect(jsonPath("$.data.userName", equalTo("foo")))
                .andExpect(jsonPath("$.data.userPassword").exists())
                .andExpect(jsonPath("$.data.createDate").isEmpty())
                .andExpect(jsonPath("$.data.updateDate").isEmpty());

    }

    @Test
    void getBoardByIdTest() throws Exception {

        //given
        BoardDto dto = new BoardDto(
                1L,
                "Spring Security 설정하기",
                "Spring Security를 활용한 인증과 인가 설정 방법을 정리.",
                "foo",
                "bar",
                null,
                null
        );

        BoardModel boardModel = BoardModel.builder()
                .id(1L)
                .title("Spring Security 설정하기")
                .content("Spring Security를 활용한 인증과 인가 설정 방법을 정리합니다.")
                .user(UserModel.builder()
                        .id(1L)
                        .userName("foo")
                        .userPassword("bar")
                        .build())
                .createDate(null)
                .updateDate(null)
                .build();

        when(boardService.getBoardById(any()))
                .thenReturn(boardModel);

        mockMvc
                //when
                .perform(get("/board/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("200")))
                .andExpect(jsonPath("$.message", equalTo("요청이 성공적으로 처리되었습니다.")))
                .andExpect(jsonPath("$.data.id", equalTo(1)))
                .andExpect(jsonPath("$.data.title", equalTo("Spring Security 설정하기")))
                .andExpect(jsonPath("$.data.content", equalTo("Spring Security를 활용한 인증과 인가 설정 방법을 정리합니다.")))
                .andExpect(jsonPath("$.data.userName", equalTo("foo")))
                .andExpect(jsonPath("$.data.userPassword").exists())
                .andExpect(jsonPath("$.data.createDate").isEmpty())
                .andExpect(jsonPath("$.data.updateDate").isEmpty());

    }

    @Test
    void updateBoardTest() throws Exception {

        //given
        BoardDto dto = new BoardDto(
                1L,
                "Spring Security 설정하기",
                "Spring Security를 활용한 인증과 인가 설정 방법을 정리.",
                "foo",
                "bar",
                null,
                null
        );

        UserModel userModel = UserModel.builder()
                .id(1L)
                .userName("foo")
                .userPassword("bar")
                .userRole(UserRole.USER)
                .build();

        BoardModel boardModel = BoardModel.builder()
                .id(1L)
                .title("Spring Security 설정하기")
                .content("Spring Security를 활용한 인증과 인가 설정 방법을 정리합니다.")
                .user(userModel)
                .createDate(null)
                .updateDate(null)
                .build();

        when(boardService.updateBoard(any(), any(BoardModel.class)))
                .thenReturn(boardModel);

        mockMvc
                //when
                .perform(put("/board/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.toJson(dto))
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("200")))
                .andExpect(jsonPath("$.message", equalTo("요청이 성공적으로 처리되었습니다.")))
                .andExpect(jsonPath("$.data.id", equalTo(1)))
                .andExpect(jsonPath("$.data.title", equalTo("Spring Security 설정하기")))
                .andExpect(jsonPath("$.data.content", equalTo("Spring Security를 활용한 인증과 인가 설정 방법을 정리합니다.")))
                .andExpect(jsonPath("$.data.userName", equalTo("foo")))
                .andExpect(jsonPath("$.data.userPassword").exists())
                .andExpect(jsonPath("$.data.createDate").isEmpty())
                .andExpect(jsonPath("$.data.updateDate").isEmpty());

    }

    @Test
    void deleteBoardTest() throws Exception {

        //given
        BoardDto dto = new BoardDto(
                1L,
                "Spring Security 설정하기",
                "Spring Security를 활용한 인증과 인가 설정 방법을 정리.",
                "foo",
                "bar",
                null,
                null
        );

        UserModel userModel = UserModel.builder()
                .id(1L)
                .userName("foo")
                .userPassword("bar")
                .build();

        BoardModel boardModel = BoardModel.builder()
                .id(1L)
                .title("Spring Security 설정하기")
                .content("Spring Security를 활용한 인증과 인가 설정 방법을 정리합니다.")
                .user(userModel)
                .createDate(null)
                .updateDate(null)
                .build();

        when(boardService.deleteBoard(any()))
                .thenReturn(boardModel);

        mockMvc
                //when
                .perform(delete("/board/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.toJson(dto))
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", equalTo("200")))
                .andExpect(jsonPath("$.message", equalTo("요청이 성공적으로 처리되었습니다.")))
                .andExpect(jsonPath("$.data.id", equalTo(1)))
                .andExpect(jsonPath("$.data.title", equalTo("Spring Security 설정하기")))
                .andExpect(jsonPath("$.data.content", equalTo("Spring Security를 활용한 인증과 인가 설정 방법을 정리합니다.")))
                .andExpect(jsonPath("$.data.userName", equalTo("foo")))
                .andExpect(jsonPath("$.data.userPassword").exists())
                .andExpect(jsonPath("$.data.createDate").isEmpty())
                .andExpect(jsonPath("$.data.updateDate").isEmpty());

    }

}