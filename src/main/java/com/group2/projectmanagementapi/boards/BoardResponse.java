package com.group2.projectmanagementapi.boards;

import java.util.List;

import com.group2.projectmanagementapi.appusers.AppUserResponse;
import com.group2.projectmanagementapi.tasks.model.Task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {

    private Long id;
    private String title;
    private List<Task> tasks;
    private List<AppUserResponse> appUsers;
    
}
