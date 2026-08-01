package com.example.jobtracker.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateFilterRequest {

    private String candidateName;
    private String graduationYear;
    private String stream;
    private String degree;
    private String phone;
    private Boolean haveLaptop;
    private Boolean haveInternet;
    private Boolean haveMobile;

}
