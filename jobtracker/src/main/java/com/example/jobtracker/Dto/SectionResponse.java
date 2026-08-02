package com.example.jobtracker.Dto;

import com.example.jobtracker.Enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionResponse {
    private Long id;
    private String sectionName;
    private String description;
    private Boolean active;

    private String userName;
    private Role role;
}
