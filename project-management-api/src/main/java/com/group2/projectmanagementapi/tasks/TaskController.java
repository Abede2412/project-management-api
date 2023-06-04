package com.group2.projectmanagementapi.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.group2.projectmanagementapi.boards.Board;
import com.group2.projectmanagementapi.boards.BoardService;
import com.group2.projectmanagementapi.tasks.model.Task;
import com.group2.projectmanagementapi.tasks.model.dto.TaskRequest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final BoardService boardService;

    @PostMapping("/board/{id}/tasks")
    public ResponseEntity<Task> createOne(@PathVariable("id") Long id, @RequestBody TaskRequest taskRequest) {
        Optional<Board> optionalBoard = this.boardService.findById(id);

        Board board = optionalBoard.get();
        Task newTask = taskRequest.convertToEntity();
        newTask.setBoard(board);

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

    @GetMapping("/boards/{id}/tasks")
    public ResponseEntity<List<Task>> getByStatus(@PathVariable("id") Long id,
            @RequestParam String status) {

        Optional<Board> board = boardService.findById(id);
        List<Task> tasks = board.get().getTasks();

        List<Task> filteredTask = tasks.stream().filter(task -> status.equals(task.getStatus()))
                .toList();

        System.out.println(filteredTask);
        return ResponseEntity.ok().body(filteredTask);
    }

    @GetMapping("/boards/{id}/tasksb")
    public ResponseEntity<List<Task>> getTaskByBoard(@PathVariable("id") Long id) {
        Optional<Board> board = boardService.findById(id);
        List<Task> tasks = board.get().getTasks();
        return ResponseEntity.ok().body(tasks);
    }

}
