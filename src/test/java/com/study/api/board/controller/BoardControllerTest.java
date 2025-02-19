package com.study.api.board.controller;

import com.study.api.auth.controller.AuthController;
import com.study.api.board.service.BoardService;
import com.study.test.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;



@Import(TestSecurityConfig.class)
@WebMvcTest(AuthController.class)
class BoardControllerTest {

    @MockitoBean
    private BoardService boardService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllBoardsTest() throws Exception {}

    @Test
    void createBoardTest() throws Exception {}

    @Test
    void getBoardByIdTest() throws Exception {}

    @Test
    void updateBoardTest() throws Exception {}

    @Test
    void deleteBoardTest() throws Exception {}

}