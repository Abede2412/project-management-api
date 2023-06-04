package com.group2.projectmanagementapi.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group2.projectmanagementapi.applicationuser.ApplicationUserService;
import com.group2.projectmanagementapi.appusers.AppUser;
import com.group2.projectmanagementapi.authentication.model.UserPrincipal;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;



@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
public class GreetingController {

    private final ApplicationUserService applicationUserService;


    @GetMapping("/greetings")
    public AppUser greet() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        AppUser appuser = this.applicationUserService.findById(userPrincipal.getId()).getAppUser();
        return appuser;
    }

}
