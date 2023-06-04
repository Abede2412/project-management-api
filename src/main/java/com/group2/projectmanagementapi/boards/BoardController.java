package com.group2.projectmanagementapi.boards;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group2.projectmanagementapi.applicationuser.ApplicationUserService;
import com.group2.projectmanagementapi.appusers.AppUser;
import com.group2.projectmanagementapi.authentication.model.UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final ApplicationUserService applicationUserService;

    @PostMapping("/appusers/boards")
    @Operation(summary = "create board")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",description = "Board is created", 
            content= @Content(mediaType = "application/json",
                schema = @Schema(implementation = Board.class),
                examples = @ExampleObject())),
        @ApiResponse(responseCode = "400", description = "invalid request")
    })
    public ResponseEntity<Board> createOne(@Valid @RequestBody BoardRequest boardRequest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        AppUser appUser = applicationUserService.findById(userPrincipal.getId()).getAppUser();

        Board board = boardRequest.convertToEntity();
        appUser.addBoard(board);
        Board newBoard = boardService.createOne(board);
        
        return ResponseEntity.status(201).body(newBoard);
        
    }

    @Operation(summary = "get boards user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "Boards is get", 
            content= @Content(mediaType = "application/json",
                schema = @Schema(implementation = BoardResponse.class),
                examples = @ExampleObject())),
        @ApiResponse(responseCode = "404", description = "Boards not found"),
        @ApiResponse(responseCode = "400", description = "invalid request")
    })
    @GetMapping("/appusers/boards")
    public ResponseEntity<Page<BoardResponse>> getBoardsByAppUsersId(@RequestParam(name = "page", required = false, defaultValue = "1") Optional<Integer> page){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        AppUser appUser = applicationUserService.findById(userPrincipal.getId()).getAppUser();
        
        Pageable pageable = PageRequest.of(page.get()-1, 10);
        Page<Board> boards = boardService.findBoardsByAppUserId(appUser.getId(), pageable);
        Page<BoardResponse> boardResponse = new PageImpl<>(boards.stream().map(Board::convertToResponse).toList(), pageable, boards.getTotalElements());
        return ResponseEntity.ok().body(boardResponse);
    }

    @GetMapping("/boards/{id}")
    @Operation(summary = "get board by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "Board is get", 
            content= @Content(mediaType = "application/json",
                schema = @Schema(implementation = BoardResponse.class),
                examples = @ExampleObject())),
        @ApiResponse(responseCode = "404", description = "Board not found"),
        @ApiResponse(responseCode = "400", description = "invalid request")
    })
    public ResponseEntity<BoardResponse> getBoardById(@PathVariable("id") Long id){
        Board board = boardService.findById(id).orElseThrow(BoardNotFoundException::new);
        return ResponseEntity.ok().body(board.convertToResponse());
    }

    @PutMapping("/boards/{id}")
    @Operation(summary = "update board by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "Board is updated", 
            content= @Content(mediaType = "application/json",
                schema = @Schema(implementation = BoardResponse.class),
                examples = @ExampleObject())),
        @ApiResponse(responseCode = "404", description = "Board not found"),
        @ApiResponse(responseCode = "400", description = "invalid request")
    })
    public ResponseEntity<BoardResponse> updateBoardById(@PathVariable("id") Long id, @Valid @RequestBody BoardRequest boardRequest){
        Board board = boardRequest.convertToEntity();
        Board updatedBoard = boardService.updateBoardById(id, board);
        return ResponseEntity.ok().body(updatedBoard.convertToResponse());
    }  

    @PostMapping("/boards/{id}/appusers/{appuserId}")
    @Operation(summary = "add user to board")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",description = "user is added to board", 
            content= @Content(mediaType = "application/json",
                schema = @Schema(implementation = BoardResponse.class),
                examples = @ExampleObject())),
        @ApiResponse(responseCode = "404", description = "Board not found or user not found"),
        @ApiResponse(responseCode = "400", description = "invalid request")
    })
    public ResponseEntity<BoardResponse> addUserById(@PathVariable("id") Long boardId, @PathVariable("appuserId") Long appUserId){
        Board existBoard = boardService.findById(boardId).orElseThrow(BoardNotFoundException::new);
        Board updatedBoard = boardService.addUserById(existBoard, appUserId);
        return ResponseEntity.ok().body(updatedBoard.convertToResponse());
    }

    @DeleteMapping("/boards/{id}/appusers/{appuserId}")
    @Operation(summary = "delete user from board")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "user is deleted from board", 
            content= @Content(mediaType = "application/json",
                schema = @Schema(implementation = BoardResponse.class),
                examples = @ExampleObject())),
        @ApiResponse(responseCode = "404", description = "Board not found or user not found"),
        @ApiResponse(responseCode = "400", description = "invalid request")
    })
    public ResponseEntity<BoardResponse> deleteUserById(@PathVariable("id")Long boardId, @PathVariable("appuserId") Long appUserId){
        Board existBoard = boardService.findById(boardId).orElseThrow(BoardNotFoundException::new);
        Board updatedBoard = boardService.deleteUserById(existBoard, appUserId);
        return ResponseEntity.ok().body(updatedBoard.convertToResponse());
    }

    @DeleteMapping("/boards/{id}")
    @Operation(summary = "delete board")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204",description = "board is deleted", 
            content= @Content(mediaType = "application/json",
                schema = @Schema(implementation = BoardResponse.class),
                examples = @ExampleObject())),
        @ApiResponse(responseCode = "404", description = "Board not found"),
        @ApiResponse(responseCode = "400", description = "invalid request")
    })
    public ResponseEntity<HttpStatus> deleteBoardsById(@PathVariable("id") Long id){
        boardService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    
}
