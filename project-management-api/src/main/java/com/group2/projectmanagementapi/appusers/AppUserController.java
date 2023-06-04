package com.group2.projectmanagementapi.appusers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AppUserController {
    
    private final AppUserService appUserService;

    @PostMapping("/register")
    public ResponseEntity<AppUserResponse> createOne(@RequestBody AppUserRequest appUserRequest){
        AppUser appUser = appUserRequest.convertToEntity();
        AppUser newAppUser = appUserService.createOne(appUser);
        AppUserResponse appUserResponse = newAppUser.convertToResponse();
        return ResponseEntity.status(201).body(appUserResponse);
        
    }
}
