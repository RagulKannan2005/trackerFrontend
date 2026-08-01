package com.example.jobtracker.Dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackerRequest {

    @NotBlank(message = "tracker name is required")
    private String trackerName;

    @NotBlank(message = "description is required")
    private String description;

    @NotNull(message = "active status is required")
    private Boolean active;

    @NotNull(message = "User id is required")
    private Long userId;
}
