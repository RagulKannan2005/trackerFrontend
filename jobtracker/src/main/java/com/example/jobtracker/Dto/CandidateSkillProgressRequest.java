package com.example.jobtracker.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CandidateSkillProgressRequest {
    private Long candidateId;
    private Long sectionSkillId;
    private String status;
    private String remarks;
}
