package com.example.jobtracker.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobtracker.Dto.TrackerSectionResponse;
import com.example.jobtracker.Service.TrackerSectionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/trackersection")
@RequiredArgsConstructor
public class TrackerSectionController {

    private final TrackerSectionService trackerSectionService;


    @GetMapping("/tracker/{trackerId}")
    public ResponseEntity<List<TrackerSectionResponse>> getSectionsByTracker(@PathVariable Long trackerId){
        return ResponseEntity.ok(trackerSectionService.getSectionsByTracker(trackerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackerSectionResponse> getTrackerSectionById(@PathVariable Long id){
        return ResponseEntity.ok(trackerSectionService.getTrackerSectionById(id));
    }

    
    
}
