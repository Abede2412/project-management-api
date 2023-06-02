package com.group2.projectmanagementapi.tasks;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group2.projectmanagementapi.applicationuser.ApplicationUserService;
import com.group2.projectmanagementapi.appusers.AppUser;
import com.group2.projectmanagementapi.authentication.model.UserPrincipal;
import com.group2.projectmanagementapi.boards.Board;
import com.group2.projectmanagementapi.boards.BoardService;
import com.group2.projectmanagementapi.tasks.model.Task;
import com.group2.projectmanagementapi.tasks.model.dto.TaskRequest;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final ApplicationUserService applicationUserService;
    private final BoardService boardService;

    @PostMapping("/tasks")
    public ResponseEntity<Task> createOne(@RequestBody TaskRequest taskRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        AppUser appUser = applicationUserService.findById(userPrincipal.getId()).getAppUser();
        Optional<Board> board = boardService.findBoardsByAppUserId(appUser.getId());

        Task newTask = taskRequest.convertToEntity();
        List<Task> tasks = board.get().getTasks();
        tasks.add(newTask);
        board.get().setTasks(tasks);
        Task savedTask = this.taskService.save(newTask);
        return ResponseEntity.ok().body(savedTask);
    }

    @DeleteMapping("task/{id}")
    public void deleteOne(@PathVariable("id") Long id) {
        Task existTask = this.taskService.findById(id);
        this.taskService.deleteOne(existTask);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<Task> updateOne(@PathVariable("id") Long id, @RequestBody TaskRequest taskRequest) {
        Task task = taskRequest.convertToEntity();
        task.setId(id);
        Task updatedTask = this.taskService.updateOne(task);
        return ResponseEntity.ok().body(updatedTask);
    }

}
