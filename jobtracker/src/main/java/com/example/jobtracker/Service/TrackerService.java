package com.example.jobtracker.Service;

import java.util.List;

import com.example.jobtracker.Dto.TrackerRequest;
import com.example.jobtracker.Dto.TrackerResponse;

public interface TrackerService {

    TrackerResponse createTracker(TrackerRequest tracker);
    List<TrackerResponse> getAllTrackers();
    TrackerResponse deleteTracker(Long id);
    TrackerResponse updateTracker(Long id,TrackerRequest tracker);
    TrackerResponse getTrackerById(Long id);
}

