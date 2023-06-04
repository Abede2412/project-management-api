package com.group2.projectmanagementapi.tasks.model.dto;

import com.group2.projectmanagementapi.tasks.model.Task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    private Long id;
    private String title;
    private String description;
    private String status;

    public Task convertToEntity() {
        return Task.builder().title(this.title).description(this.description).status(this.status).build();
    }

    @Override
    public String toString() {
        return "TaskRequest [id=" + id + ", title=" + title + ", description=" + description + ", status=" + status
                + "]";
    }
}
