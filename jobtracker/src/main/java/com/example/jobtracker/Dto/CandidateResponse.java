package com.example.jobtracker.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateResponse {
    private Long id;
    private String candidateName;
    private String degree;
    private String stream;
    private String graduationYear;
    private String phone;
    private Boolean haveLaptop;
    private Boolean haveInternet;
    private Boolean haveMobile;
    private String englishSpeaking;
    private String englishWriting;
    private String englishReading;

    private Long userId;
    private String username;
    private Long trackerId;
    private String trackerName;
    private TrackerDetailResponse trackerDetails;
}
