package com.group2.projectmanagementapi.appusers;

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
        ApplicationUser applicationUser = appUser.getApplicationUser();
        String hashPassword = bCryptPasswordEncoder.encode(applicationUser.getPassword());
        applicationUser.setPassword(hashPassword);
        return appUserRepository.save(appUser);
    }

    public AppUser findById(Long id) {
        return appUserRepository.getReferenceById(id);
    }

}
