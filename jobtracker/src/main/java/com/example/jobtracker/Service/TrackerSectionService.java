package com.example.jobtracker.Service;

import java.util.List;

import com.example.jobtracker.Dto.TrackerSectionRequest;
import com.example.jobtracker.Dto.TrackerSectionResponse;

public interface TrackerSectionService {

    TrackerSectionResponse assignSectionToTracker(TrackerSectionRequest request);

    List<TrackerSectionResponse> getSectionsByTracker(Long trackerId);

    TrackerSectionResponse getTrackerSectionById(Long id);

    TrackerSectionResponse updateTrackerSection(Long id, TrackerSectionRequest request);

    void removeSectionFromTracker(Long id);

}