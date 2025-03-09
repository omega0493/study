package com.study.api.board.repository;

import com.study.entity.board.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

//    @EntityGraph(attributePaths = {"user"})
//    Optional<Board> findById(Long id);

    @Query("SELECT b FROM Board b JOIN FETCH b.user")
    List<Board> findAllWithUser();
}
