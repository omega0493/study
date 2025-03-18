package com.study.api.board.service;

import com.study.api.auth.repository.UserRepository;
import com.study.api.board.repository.BoardRepository;
import com.study.entity.board.Board;
import com.study.entity.user.User;
import com.study.infra.common.exception.BusinessError;
import com.study.infra.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    // 전체 게시글 목록 조회
    public List<Board> getAllBoards() {
        return boardRepository.findAllWithUser().stream()
                .sorted(Comparator.comparing(Board::getLastModifiedAt).reversed())
                .toList();
    }

    // 게시글 작성
    @Transactional
    public Board createBoard(Board board) {

        // 회원 조회
        Optional<User> user = userRepository.findByUserName(board.getUser().getUserName());

        // 회원 존재 확인
        if (user.isEmpty()) {
            throw new BusinessException(BusinessError.NO_REGISTERED_USER);
        }

        board.setUser(user.get());

        boardRepository.save(board);

        return board;
    }

    // 선택한 게시글 조회
    public Board getBoardById(Long id) {

        Optional<Board> board = boardRepository.findById(id);

        if (board.isEmpty()) {
            throw new BusinessException(BusinessError.NO_REGISTERED_BOARD);
        }

        return board.get();
    }

    // 선택한 게시글 수정
    @Transactional
    public Board updateBoard(Long id, Board board) {

        Board foundBoard = boardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BusinessError.NO_REGISTERED_USER));
        foundBoard.edit(board);

        return foundBoard;
    }

    // 선택한 게시글 삭제
    @Transactional
    public Board deleteBoard(Long id) {

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(BusinessError.NO_REGISTERED_BOARD));
        boardRepository.deleteById(id);

        return board;
    }

}
