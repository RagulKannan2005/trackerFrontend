package com.example.jobtracker.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingResponse {

    private Long id;
    private String eventId;
    private String summary;
    private String description;
    private String date;
    private String time;
    private String duration;
    private String participants;
    private String location;
    private String meetLink;
    private String htmlLink;
    private String startDateTime;
    private String endDateTime;
    private String status;
}
