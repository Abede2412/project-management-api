package com.group2.projectmanagementapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.group2.projectmanagementapi.appusers.image.ImageService;

import jakarta.annotation.Resource;

@SpringBootApplication
public class ProjectManagementApiApplication implements CommandLineRunner {

	@Resource
	ImageService imageService;

	public static void main(String[] args) {
		SpringApplication.run(ProjectManagementApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		imageService.deleteAll();
		imageService.init();
	}

}
