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
    private Long sectionId;
    private Integer displayOrder;

    // private String trackerName;
    // private String sectionName;
}
