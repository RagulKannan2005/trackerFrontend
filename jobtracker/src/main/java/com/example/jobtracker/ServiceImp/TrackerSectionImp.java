package com.example.jobtracker.ServiceImp;

import org.springframework.stereotype.Service;

import com.example.jobtracker.Service.TrackerSectionService;
import com.example.jobtracker.Dto.TrackerSectionRequest;
import com.example.jobtracker.Dto.TrackerSectionResponse;
import com.example.jobtracker.Entity.Section;
import com.example.jobtracker.Entity.TrackerSection;
import com.example.jobtracker.Entity.Trackers;
import com.example.jobtracker.Repository.SectionRepository;
import com.example.jobtracker.Repository.TrackerRepository;
import com.example.jobtracker.Repository.TrackerSectionRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class TrackerSectionImp implements TrackerSectionService {

    private final SectionRepository sectionRepo;
    private final TrackerRepository trackersRepo;
    private final TrackerSectionRepository trackerSectionrepo;

    @Override
    public TrackerSectionResponse assignSectionToTracker(TrackerSectionRequest request) {
        Trackers tracker=trackersRepo.findById(request.getTrackerId()).orElseThrow(()->new RuntimeException("tracker id Not Found"));
        Section section=sectionRepo.findById(request.getSectionId()).orElseThrow(()->new RuntimeException("section id Not Found"));

        TrackerSection ts = TrackerSection.builder()
                .tracker(tracker)
                .section(section)
                .displayOrder(request.getDisplayOrder())
                .build();

        TrackerSection saved = trackerSectionrepo.save(ts);
        return toDto(saved);
    }

    @Override
    public List<TrackerSectionResponse> getSectionsByTracker(Long trackerId) {
        return trackerSectionrepo.findByTrackerId(trackerId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public TrackerSectionResponse getTrackerSectionById(Long id){
        TrackerSection ts=trackerSectionrepo.findById(id).orElseThrow(()->new RuntimeException("Tracker Section not found"));
        return toDto(ts);
    }

    @Override
    public TrackerSectionResponse updateTrackerSection(Long id, TrackerSectionRequest request) {
        TrackerSection ts = trackerSectionrepo.findById(id).orElseThrow(() -> new RuntimeException("Tracker Section not found"));
        if (request.getTrackerId() != null) {
            Trackers tracker = trackersRepo.findById(request.getTrackerId()).orElseThrow(() -> new RuntimeException("tracker id Not Found"));
            ts.setTracker(tracker);
        }
        if (request.getSectionId() != null) {
            Section section = sectionRepo.findById(request.getSectionId()).orElseThrow(() -> new RuntimeException("section id Not Found"));
            ts.setSection(section);
        }
        if (request.getDisplayOrder() != null) {
            ts.setDisplayOrder(request.getDisplayOrder());
        }
        TrackerSection updated = trackerSectionrepo.save(ts);
        return toDto(updated);
    }

    @Override
    public void removeSectionFromTracker(Long id) {
        if (!trackerSectionrepo.existsById(id)) {
            throw new RuntimeException("Tracker Section not found");
        }
        trackerSectionrepo.deleteById(id);
    }   

    TrackerSectionResponse toDto(TrackerSection ts){
        return TrackerSectionResponse.builder()
        .id(ts.getId())
        .trackerId(ts.getTracker().getId())
        .sectionId(ts.getSection().getId())
        .displayOrder(ts.getDisplayOrder())
        .build();
    }
    
}
