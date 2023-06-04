package com.group2.projectmanagementapi.appusers;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.group2.projectmanagementapi.applicationuser.ApplicationUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AppUser createOne(AppUser appUser) {
        String username = appUser.getUsername();
        String email = appUser.getEmail();

        // Check if username already exists
        if (appUserRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check if email already exists
        if (appUserRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        ApplicationUser applicationUser = appUser.getApplicationUser();
        String hashPassword = bCryptPasswordEncoder.encode(applicationUser.getPassword());
        applicationUser.setPassword(hashPassword);
        return appUserRepository.save(appUser);
    }
    
    public Optional<AppUser> findByUsernameOrEmail(String usernameOrEmail) {
        return appUserRepository.findByUsernameOrEmail(usernameOrEmail);
    }

    public AppUser findById(Long id) {
        return appUserRepository.findById(id).orElseThrow(AppUserNotFoundException::new);
    }

    public AppUser updateOne(AppUser appUser) {
        return appUserRepository.save(appUser);
    }    
}
