package com.example.jobtracker.Dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillDetailResponse {
    private Long sectionSkillId;
    private Long skillId;
    private String skillName;
    private String description;
    private Integer displayOrder;
    private Long progressId;
    private String status;
    private String remarks;
    private LocalDate progressUpdatedAt;
}
