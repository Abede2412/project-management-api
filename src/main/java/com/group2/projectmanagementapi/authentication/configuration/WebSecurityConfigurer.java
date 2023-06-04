package com.group2.projectmanagementapi.authentication.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.group2.projectmanagementapi.applicationuser.ApplicationUserService;
import com.group2.projectmanagementapi.authentication.jwt.JwtAuthenticationEntryPoint;
import com.group2.projectmanagementapi.authentication.jwt.JwtAuthenticationTokenFilter;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfigurer {
    private final ApplicationUserService userDetailService;

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    JwtAuthenticationTokenFilter authenticationJwtTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    AuthenticationManager authenticationManagerBean(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(this.userDetailService)
                .passwordEncoder(this.bCryptPasswordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests().requestMatchers("/login").permitAll().and()
                .authorizeHttpRequests().requestMatchers("/register").permitAll().and()
                .authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/appusers").permitAll().and()
                .authorizeHttpRequests().requestMatchers("/logout").permitAll().and()
                .authorizeHttpRequests().anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/swagger-ui/**", "/v3/api-docs/**");
    }
}
