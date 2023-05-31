package com.group2.projectmanagementapi.appusers;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.group2.projectmanagementapi.applicationuser.ApplicationUser;
import com.group2.projectmanagementapi.boards.Board;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
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
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String username;

    @Builder.Default
    private String imageUrl = "default";

    @OneToOne
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = "application_user_id")
    private ApplicationUser applicationUser;
 
    @ManyToMany
    @JoinTable(name = "board_take",
        joinColumns = @JoinColumn(name = "app_user_id"),
        inverseJoinColumns = @JoinColumn(name = "board_id"))
    private List<Board> takenBoards;

    public AppUserResponse convertToResponse() {
        return AppUserResponse.builder()
            .id(id)
            .name(name)
            .email(email)
            .username(username)
            .imageUrl(imageUrl)
            .build();
    }
 
}
