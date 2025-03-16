package com.study.api.board.service;

import com.study.api.auth.service.AuthService;
import com.study.api.board.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class BoardServiceTest {

    @MockitoBean
    private BoardRepository boardRepository;

    @Autowired
    private AuthService sut;

    @Test
    void getAllBoardsTest() {
    }

    @Test
    void createBoardTest() {
    }

    @Test
    void getBoardByIdTest() {
    }

    @Test
    void updateBoardTest() {
    }

    @Test
    void deleteBoardTest() {
    }
}