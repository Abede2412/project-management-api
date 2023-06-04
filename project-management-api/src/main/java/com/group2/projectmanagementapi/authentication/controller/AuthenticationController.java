package com.group2.projectmanagementapi.authentication.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.group2.projectmanagementapi.authentication.jwt.JwtProvider;
import com.group2.projectmanagementapi.authentication.model.dto.JwtResponseDto;
import com.group2.projectmanagementapi.authentication.model.dto.LoginRequestDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody LoginRequestDto LoginRequestDto) {
        logger.info("Attempt login to system: {}", LoginRequestDto);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(LoginRequestDto.getUsernameOrEmail(), LoginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);

        return ResponseEntity.ok(new JwtResponseDto(jwt));
    }
}
