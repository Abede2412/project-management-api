package com.group2.projectmanagementapi.tasks;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group2.projectmanagementapi.tasks.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
