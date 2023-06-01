package com.group2.projectmanagementapi.boards;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group2.projectmanagementapi.appusers.AppUser;
import com.group2.projectmanagementapi.tasks.model.Task;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @OneToMany(mappedBy = "board")
    private List<Task> tasks;

    @ManyToMany(mappedBy = "boards",
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        })
    @JsonIgnore
    @Builder.Default
    private Set<AppUser> appUsers = new HashSet<>();

    public void addAppUser(AppUser appUser){
        this.appUsers.add(appUser);
        appUser.getBoards().add(this);
    }
    
}
