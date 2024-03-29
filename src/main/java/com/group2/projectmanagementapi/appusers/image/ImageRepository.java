package com.group2.projectmanagementapi.appusers.image;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    
    Optional<Image> findByName(String name);
}
