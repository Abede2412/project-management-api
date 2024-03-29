package com.group2.projectmanagementapi.boards;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findBoardsByAppUsersId(Long id, Pageable pageable);

    Optional<Board> findBoardsByAppUsersId(Long id);

}
