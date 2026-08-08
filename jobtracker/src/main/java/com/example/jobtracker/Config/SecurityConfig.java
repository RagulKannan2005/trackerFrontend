package com.example.jobtracker.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.jobtracker.Security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**", "/error").permitAll()
                        .requestMatchers("/api/v1/users/password/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/v1/users/email/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/v1/users/allusers/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/v1/users/role/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/v1/candidates/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/v1/trackers/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/v1/section/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/trackersection/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/v1/skills/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/section-skills/**").hasAnyRole("ADMIN","USER")
                        .requestMatchers("/api/v1/candidate-skill-progress/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/api/v1/google-meet/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
