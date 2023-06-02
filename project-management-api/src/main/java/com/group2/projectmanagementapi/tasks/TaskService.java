package com.group2.projectmanagementapi.tasks;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.group2.projectmanagementapi.boards.BoardService;
import com.group2.projectmanagementapi.tasks.exception.TaskNotFoundException;
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

    public void deleteOne(Task task) {
        this.taskRepository.delete(task);
    }

    public Task findById(Long id) {
        Optional<Task> optionalTask = this.taskRepository.findById(id);
        if (!optionalTask.isPresent()) {
            throw new TaskNotFoundException("Task not found.");
        }
        return optionalTask.get();
    }

    public Task updateOne(Task task) {
        Task existingTask = this.findById(task.getId());

        task.setId(existingTask.getId());
        Task updatedTask = this.taskRepository.save(task);
        return updatedTask;
    }

}
