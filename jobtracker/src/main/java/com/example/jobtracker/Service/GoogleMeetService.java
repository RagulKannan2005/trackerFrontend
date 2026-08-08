package com.example.jobtracker.Service;

import java.util.List;

import com.example.jobtracker.Dto.MeetingRequest;
import com.example.jobtracker.Dto.MeetingResponse;

public interface GoogleMeetService {
    MeetingResponse createMeeting(MeetingRequest request);
    List<MeetingResponse> getAllMeetings();
    MeetingResponse getMeetingById(Long id);
}
