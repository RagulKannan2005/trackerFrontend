package com.example.jobtracker.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionRequest {

    @NotBlank(message="section name is required")
    private String sectionName;

    @NotBlank(message="description is required")
    private String description;

    @NotNull(message="active status is required")
    private Boolean active;

    @NotNull(message = "user id is required")
    private Long userId;
}
