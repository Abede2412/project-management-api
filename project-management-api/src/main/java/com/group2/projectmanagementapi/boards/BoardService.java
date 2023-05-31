package com.group2.projectmanagementapi.boards;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Board createOne(Board board){
        return boardRepository.save(board);
    }

    public Page<Board> findBoardsByAppUserId(Long id, Pageable pageable) {
        return boardRepository.findBoardsByAppUsersId(id, pageable);
    }

    public Optional<Board> findById(Long id) {
        return boardRepository.findById(id);
    }
    
}
