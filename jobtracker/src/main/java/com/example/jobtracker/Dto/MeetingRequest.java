package com.example.jobtracker.Dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingRequest {

    @NotBlank(message = "Summary is required")
    private String summary;

    private String description;

    @NotBlank(message = "Start date time is required (ISO format, e.g. 2026-08-08T10:00:00)")
    private String startDateTime;

    @NotBlank(message = "End date time is required (ISO format, e.g. 2026-08-08T11:00:00)")
    private String endDateTime;

    private List<String> attendeeEmails;
}
