package com.group2.projectmanagementapi.tasks;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.group2.projectmanagementapi.boards.Board;
import com.group2.projectmanagementapi.tasks.exception.TaskNotFoundException;
import com.group2.projectmanagementapi.tasks.model.Task;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

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
        Board existingBoard = existingTask.getBoard();
        task.setId(existingTask.getId());
        task.setBoard(existingBoard);
        Task updatedTask = this.taskRepository.save(task);
        return updatedTask;
    }
}
