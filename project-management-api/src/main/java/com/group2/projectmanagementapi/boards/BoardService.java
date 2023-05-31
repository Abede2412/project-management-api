package com.group2.projectmanagementapi.boards;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Board createOne(Board board){
        return boardRepository.save(board);
    }
    
}
