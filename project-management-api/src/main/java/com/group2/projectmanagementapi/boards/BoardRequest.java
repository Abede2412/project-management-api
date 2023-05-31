package com.group2.projectmanagementapi.boards;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequest {

    private String title;

    public Board convertToEntity() {
        return null;
    }

}
