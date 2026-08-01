package com.example.jobtracker.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateRequest {

    @NotBlank(message = "candidate name is required")
    private String candidateName;

    @NotBlank(message = "degree is required")
    private String degree;

    @NotBlank(message = "stream is required")
    private String stream;

    @NotBlank(message = "graduation year is required")
    private String graduationYear;

    @NotBlank(message = "phone is required")
    @Pattern(regexp = "^\\d{10}$", message = "Enter a valid 10-digit phone number")
    private String phone;

    @NotNull(message = "Having laptop is required")
    private Boolean haveLaptop;

    @NotNull(message = "Having internet is required")
    private Boolean haveInternet;

    @NotNull(message = "Having mobile is required")
    private Boolean haveMobile;

    @NotBlank(message = "english speaking is required")
    private String englishSpeaking;

    @NotBlank(message = "english writing is required")
    private String englishWriting;

    @NotBlank(message = "english reading is required")
    private String englishReading;

    @NotNull(message = "user_id is required to store the candidate")
    private Long userId;



}
