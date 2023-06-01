package com.group2.projectmanagementapi.tasks;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group2.projectmanagementapi.tasks.model.Task;
import com.group2.projectmanagementapi.tasks.model.dto.TaskRequest;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TaskController {
    public TaskService taskService;

    @PostMapping("/tasks")
    public ResponseEntity<Task> createOne(@RequestBody TaskRequest taskRequest) {
        Task newTask = taskRequest.convertToEntity();
        Task savedTask = this.taskService.save(newTask);
        return ResponseEntity.ok().body(savedTask);
    }

}
