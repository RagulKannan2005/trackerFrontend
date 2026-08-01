package com.example.jobtracker.Service;

import java.util.List;

import com.example.jobtracker.Dto.TrackerRequest;
import com.example.jobtracker.Dto.TrackerResponse;

public interface TrackerService {

    TrackerResponse createTracker(TrackerRequest tracker);
    List<TrackerResponse> getAllTrackers();
}
