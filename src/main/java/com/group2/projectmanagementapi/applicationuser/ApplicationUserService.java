package com.group2.projectmanagementapi.applicationuser;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.group2.projectmanagementapi.authentication.model.UserPrincipal;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ApplicationUserService implements UserDetailsService {
    private final ApplicationUserRepository applicationUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) {
        ApplicationUser applicationUser = applicationUserRepository.findByUsernameOrEmail(usernameOrEmail).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> username or email" + usernameOrEmail));
        return UserPrincipal.build(applicationUser);
    }

    public ApplicationUser findById(UUID id){
        return applicationUserRepository.getReferenceById(id);
    }

    public ApplicationUser findByUsername(String username){
        return applicationUserRepository.findByUsername(username);
    }

    public ApplicationUser findByEmail(String email){
        return applicationUserRepository.findByEmail(email);
    }

}
