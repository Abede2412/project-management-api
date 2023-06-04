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

    // public AppUser createOne(AppUser appUser, byte[] imageData) {
    //     Image image = new Image();
    //     image.setData(imageData);
    //     appUser.setImage(image);
    //     image.setAppUser(appUser);
    
    //     ApplicationUser applicationUser = appUser.getApplicationUser();
    //     String hashPassword = bCryptPasswordEncoder.encode(applicationUser.getPassword());
    //     applicationUser.setPassword(hashPassword);
    
    //     return appUserRepository.save(appUser);
    // }
    
    // public AppUser updateImageUrl(AppUser appUser, byte[] imageData) {
    //     Image image = appUser.getImage();
    //     if (image == null) {
    //         image = new Image();
    //         appUser.setImage(image);
    //         image.setAppUser(appUser);
    //     }
    //     image.setData(imageData);
    
    //     return appUserRepository.save(appUser);
    // }
    

}
