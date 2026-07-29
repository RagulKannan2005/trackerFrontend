package com.example.jobtracker.ServiceImp;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.jobtracker.Dto.UserRequestDto;
import com.example.jobtracker.Dto.UserResponseDto;
import com.example.jobtracker.Entity.Role;
import com.example.jobtracker.Entity.Users;
import com.example.jobtracker.Repository.UserRepository;
import com.example.jobtracker.Service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createUser(UserRequestDto user) {
        Users newUser = Users.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(Role.valueOf(user.getRole().toUpperCase()))
                .active(user.getActive())
                .build();

        Users savedUser = userRepository.save(newUser);

        return toDto(savedUser);
    }

    private UserResponseDto toDto(Users s){
        return  UserResponseDto.builder()
                .id(s.getId())
                .username(s.getUsername())
                .email(s.getEmail())
                .password(s.getPassword())
                .role(s.getRole())
                .active(s.getActive())
                .build();
        
    }
}
