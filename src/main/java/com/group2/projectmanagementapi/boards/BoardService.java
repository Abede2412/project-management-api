package com.group2.projectmanagementapi.boards;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.group2.projectmanagementapi.appusers.AppUser;
import com.group2.projectmanagementapi.appusers.AppUserNotFoundException;
import com.group2.projectmanagementapi.appusers.AppUserRepository;
import com.group2.projectmanagementapi.appusers.AppUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final AppUserService appUserService;

    public Board createOne(Board board) {
        return boardRepository.save(board);
    }

    public Page<Board> findBoardsByAppUserId(Long id, Pageable pageable) {
        return boardRepository.findBoardsByAppUsersId(id, pageable);
    }

    public Optional<Board> findBoardsByAppUserId(Long id) {
        return boardRepository.findBoardsByAppUsersId(id);
    }

    public Optional<Board> findById(Long id) {
        return boardRepository.findById(id);
    }

    public Board updateBoardById(Long id, Board board) {
        Board existBoard = findById(id).orElseThrow(BoardNotFoundException::new);
        existBoard.setTitle(board.getTitle());
        return boardRepository.save(existBoard);
    }

    public Board addUserById(Board existBoard, Long appUserId) {
        AppUser appUser = appUserService.findById(appUserId);
        if (appUser == null){
            throw new AppUserNotFoundException();
        }
        existBoard.addAppUser(appUser);
        return boardRepository.save(existBoard);
    }

    public Board deleteUserById(Board existBoard, Long appUserId) {
        existBoard.deleteUser(appUserId);
        return boardRepository.save(existBoard);
    }

    public void deleteById(Long id) {
        Board existBoard = findById(id).orElseThrow(BoardNotFoundException::new);
        existBoard.getAppUsers().stream().forEach(appUser -> appUser.getBoards().remove(existBoard));
        boardRepository.deleteById(id);
    }
}
