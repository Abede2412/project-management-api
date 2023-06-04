package com.group2.projectmanagementapi.appusers;

import com.group2.projectmanagementapi.applicationuser.ApplicationUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserRequest {

    private String name;
    private String email;
    private String username;
    private String password;

    public AppUser convertToEntity(){

        AppUser appUser = AppUser.builder()
            .name(name)
            .email(email)
            .username(username)
            .build();

        ApplicationUser applicationUser = ApplicationUser.builder()
            .email(email)
            .username(username)
            .password(password)
            .build();

        appUser.setApplicationUser(applicationUser);
        applicationUser.setAppUser(appUser);
        return appUser;
    }

}
