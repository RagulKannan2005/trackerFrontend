package com.example.jobtracker.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackerSectionResponse {

    private Long id;
    private Long trackerId;
    private String trackerName;
    private Long sectionId;
    private String sectionName;
    private String description;
    private Integer displayOrder;
}
