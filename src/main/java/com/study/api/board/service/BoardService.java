package com.study.api.board.service;

import com.study.api.board.model.BoardModel;
import com.study.infra.common.exception.BusinessError;
import com.study.infra.common.exception.BusinessException;
import com.study.entity.board.Board;
import com.study.api.board.repository.BoardRepository;
import com.study.entity.user.User;
import com.study.api.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    // 전체 게시글 목록 조회
    public List<BoardModel> getAllBoards() {

        List<Board> board = boardRepository.findAll();

        List<BoardModel> boardModels = new ArrayList<>();

        for (Board boardItem : board) {

            // entity -> dto
            BoardModel boardModel = BoardModel.fromEntity(boardItem);

            boardModels.add(boardModel);
        }

        return boardModels.stream()
                .sorted(Comparator.comparing(BoardModel::getUpdateDate).reversed())
                .toList();
    }

    // 게시글 작성
    @Transactional
    public BoardModel createBoard(BoardModel boardModel) {

        // model -> entity
        Board board = boardModel.toEntity(boardModel);

        // 회원 조회
        Optional<User> user = userRepository.findByUserName(board.getUser().getUserName());

        // 회원 존재 확인
        if(user.isEmpty()) {
            throw  new BusinessException(BusinessError.NO_REGISTERED_USER);
        }

        // 회원 비밀 번호 확인
        if(!board.getUser().getUserPassword().equals(user.get().getUserPassword())) {
            throw new BusinessException(BusinessError.PASSWORD_MISMATCH);
        }

        boardRepository.save(board);

        // entity -> model
        return BoardModel.fromEntity(board);
    }

    // 선택한 게시글 조회
    public BoardModel getBoardById(Long id) {

        Optional<Board> board = boardRepository.findById(id);

        if(board.isEmpty()) {
            throw new BusinessException(BusinessError.NO_REGISTERED_BOARD);
        }

        return BoardModel.fromEntity(board.get());
    }

    // 선택한 게시글 수정
    @Transactional
    public BoardModel updateBoard(Long id, BoardModel boardModel) {

        // model -> entity
        Board boardEntity = boardModel.toEntity(boardModel);

        Optional<Board> boardById = boardRepository.findById(id);

        checkBoardAccess(boardById);

        Board board = boardById.get();

        board.edit(boardEntity);

        // entity -> model
        return BoardModel.fromEntity(board);
    }

    // 선택한 게시글 삭제
    @Transactional
    public BoardModel deleteModel(Long id, BoardModel boardModel) {

        Optional<Board> boardById = boardRepository.findById(id);

        checkBoardAccess(boardById);

        boardRepository.deleteById(id);

        return boardModel;
    }

    private void checkBoardAccess(Optional<Board> boardById) {

        // 게시물 존재 확인
        if(boardById.isEmpty()) {
            throw new BusinessException(BusinessError.NO_REGISTERED_BOARD);
        }

        // 회원 조회
        Optional<User> user = userRepository.findByUserName(boardById.get().getUser().getUserName());

        // 회원 존재 확인
        if(user.isEmpty()) {
            throw  new BusinessException(BusinessError.NO_REGISTERED_USER);
        }

        // 회원 비밀 번호 확인
        if(!boardById.get().getUser().getUserPassword().equals(user.get().getUserPassword())) {
            throw new BusinessException(BusinessError.PASSWORD_MISMATCH);
        }
    }
}
