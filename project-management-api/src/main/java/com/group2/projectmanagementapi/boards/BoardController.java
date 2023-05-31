package com.group2.projectmanagementapi.boards;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private ResponseEntity<Board> createOne(@RequestBody BoardRequest boardRequest){
        Board board = boardRequest.convertToEntity();
        return null;
        
    }



    
}
