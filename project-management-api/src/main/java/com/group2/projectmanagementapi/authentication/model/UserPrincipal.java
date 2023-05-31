package com.group2.projectmanagementapi.authentication.model;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group2.projectmanagementapi.applicationuser.ApplicationUser;

import lombok.Getter;
public class UserPrincipal implements UserDetails {

    @Getter
    private UUID id;


    @Getter
    private String name;

    private String username;

    @Getter
    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private UserPrincipal(UUID id, String name, String username, String email, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = Collections.emptyList();
    }

    public static UserPrincipal build(ApplicationUser applicationUser) {
        return new UserPrincipal(applicationUser.getId(), applicationUser.getAppUser().getName(), applicationUser.getUsername(),
                applicationUser.getEmail(), applicationUser.getPassword());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
