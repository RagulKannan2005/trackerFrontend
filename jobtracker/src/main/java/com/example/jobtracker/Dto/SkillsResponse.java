package com.example.jobtracker.Dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillsResponse {
    private Long id;
    private String skillName;
    private String description;
    private Boolean active;
}
