package com.example.jobtracker.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateSkillProgressResponse {

    private Long id;
    private Long candidateId;
    private Long sectionSkillId;
    private String status;
    private String remarks;
      
}
