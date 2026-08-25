package com.example.jobtracker.Dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidatePatchRequest {

    private String candidateName;
    private String degree;
    private String stream;
    private String graduationYear;
    @Pattern(regexp = "^\\d{10}$", message = "Enter a valid 10-digit phone number")
    private String phone;
    private Boolean haveLaptop;
    private Boolean haveInternet;
    private Boolean haveMobile;
    private String englishSpeaking;
    private String englishWriting;
    private String englishReading;
    private Long trackerId;

    // Backward compatibility for legacy JSON property name
    public String getGreadutionYear() {
        return graduationYear;
    }

    public void setGreadutionYear(String greadutionYear) {
        if (this.graduationYear == null) {
            this.graduationYear = greadutionYear;
        }
    }

}
