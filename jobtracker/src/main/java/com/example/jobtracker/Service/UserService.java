package com.example.jobtracker.Service;

import com.example.jobtracker.Dto.UserRequestDto;
import com.example.jobtracker.Dto.UserResponseDto;

public interface UserService {
    UserResponseDto createUser(UserRequestDto user);
}
