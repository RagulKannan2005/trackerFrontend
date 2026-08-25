package com.example.jobtracker.Dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionDetailResponse {
    private Long trackerSectionId;
    private Long sectionId;
    private String sectionName;
    private String description;
    private Integer displayOrder;
    private List<SkillDetailResponse> skills;
}
