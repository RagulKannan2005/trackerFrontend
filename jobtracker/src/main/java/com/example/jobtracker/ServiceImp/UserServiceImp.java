package com.example.jobtracker.ServiceImp;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public UserResponseDto updateemail(Long id, UserRequestDto request) {
        Users existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (request.getUsername() != null) {
            existingUser.setUsername(request.getUsername());
        }
        if (request.getEmail() != null) {
            existingUser.setEmail(request.getEmail());
        }
        if (request.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRole() != null) {
            existingUser.setRole(Role.valueOf(request.getRole().toUpperCase()));
        }
        if (request.getActive() != null) {
            existingUser.setActive(request.getActive());
        }

        Users savedUser=userRepository.save(existingUser);

        return toDto(savedUser);

    }

    @Override
    public UserResponseDto updatePassword(Long id, UserRequestDto request) {
        Users existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        
        if(request.getPassword()!=null){
            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        Users savedUser=userRepository.save(existingUser);
        return toDto(savedUser);
        
    }

    @Override
    public List<UserResponseDto> getAllUsers(){
        List<Users> users=userRepository.findAll();
        return users.stream().map(this::toDto).collect(Collectors.toList());
    }

    private UserResponseDto toDto(Users s) {
        return UserResponseDto.builder()
                .id(s.getId())
                .username(s.getUsername())
                .email(s.getEmail())
                .password(s.getPassword())
                .role(s.getRole())
                .active(s.getActive())
                .build();

    }

    @Override
    public List<UserResponseDto> findByRole(String role) {
        List<Users> users=userRepository.findByRole("USER");
        return users.stream().map(this::toDto).collect(Collectors.toList());
    }
}
