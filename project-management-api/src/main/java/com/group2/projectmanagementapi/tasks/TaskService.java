package com.group2.projectmanagementapi.tasks;

import org.springframework.stereotype.Service;

import com.group2.projectmanagementapi.boards.BoardService;
import com.group2.projectmanagementapi.tasks.model.Task;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final BoardService boardService;

    public Task save(Task task) {
        Task savedTask = this.taskRepository.save(task);
        return savedTask;
    }

    public void delete(Task task) {
        this.taskRepository.delete(task);
    }

}
