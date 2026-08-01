package com.example.jobtracker.Service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.jobtracker.Dto.AuthenticationRequest;
import com.example.jobtracker.Dto.AuthenticationResponse;
import com.example.jobtracker.Dto.RegisterRequest;
import com.example.jobtracker.Entity.Users;
import com.example.jobtracker.Enums.Role;
import com.example.jobtracker.Repository.UserRepository;
import com.example.jobtracker.Security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request){
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        var user= Users.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole() != null ? request.getRole() : Role.USER)
                    .active(true)
                    .build();

        userRepository.save(user);
        var jwtToken=jwtService.generateToken(user);
        return AuthenticationResponse.builder()
            .token(jwtToken)
            .username(user.getRealUsername())
            .role(user.getRole())
            .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Users user = (Users) authentication.getPrincipal();
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(user.getRealUsername())
                .role(user.getRole())
                .build();
    }
    
}
