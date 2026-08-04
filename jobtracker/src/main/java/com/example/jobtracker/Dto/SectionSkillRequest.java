package com.example.jobtracker.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionSkillRequest {

    private Long trackerSectionId;
    private Long skillId;
    private Integer displayOrder;
    
}
