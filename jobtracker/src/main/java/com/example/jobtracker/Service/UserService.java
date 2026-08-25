package com.example.jobtracker.Service;

import com.example.jobtracker.Dto.UserRequestDto;
import com.example.jobtracker.Dto.UserResponseDto;
import java.util.List;
public interface UserService {
    UserResponseDto createUser(UserRequestDto user);

    UserResponseDto updateemail(Long id, UserRequestDto user);
    UserResponseDto updatePassword(Long id,UserRequestDto user);
    List<UserResponseDto> getAllUsers();
    List<UserResponseDto> findByRole(String role);

}
