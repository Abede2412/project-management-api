package com.group2.projectmanagementapi.appusers;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppUserRepository extends JpaRepository<AppUser, Long>{

    @Query("select a from AppUser a where a.username = ?1 or a.email = ?1")
    Optional<AppUser> findByUsernameOrEmail(String usernameOrEmail);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
