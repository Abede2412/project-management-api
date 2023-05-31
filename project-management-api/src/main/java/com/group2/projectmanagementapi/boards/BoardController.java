package com.group2.projectmanagementapi.boards;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group2.projectmanagementapi.applicationuser.ApplicationUserService;
import com.group2.projectmanagementapi.appusers.AppUser;
import com.group2.projectmanagementapi.authentication.model.UserPrincipal;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final ApplicationUserService applicationUserService;

    @PostMapping("/appusers/boards")
    private ResponseEntity<Board> createOne(@RequestBody BoardRequest boardRequest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        AppUser appUser = applicationUserService.findById(userPrincipal.getId()).getAppUser();

        Board board = boardRequest.convertToEntity();
        appUser.addBoard(board);
        Board newBoard = boardService.createOne(board);
        
        return ResponseEntity.status(201).body(newBoard);
        
    }

    @GetMapping("/appusers/boards")
    private ResponseEntity<Page<Board>> getBoardsByAppUsersId(@RequestParam(name = "page", required = false, defaultValue = "1") Optional<Integer> page){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        AppUser appUser = applicationUserService.findById(userPrincipal.getId()).getAppUser();
        
        Pageable pageable = PageRequest.of(page.get()-1, 10);
        Page<Board> boards = boardService.findBoardsByAppUserId(appUser.getId(), pageable);
        return ResponseEntity.ok().body(boards);
    }

    @GetMapping("/boards/{id}")
    private ResponseEntity<Board> getBoardById(@PathVariable("id") Long id){
        Board board = boardService.findById(id).orElseThrow(BoardNotFoundException::new);
        return ResponseEntity.ok().body(board);
    }



    
}
