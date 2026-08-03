package com.example.jobtracker.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobtracker.Dto.TrackerSectionRequest;
import com.example.jobtracker.Dto.TrackerSectionResponse;
import com.example.jobtracker.Service.TrackerSectionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/trackersection")
@RequiredArgsConstructor
public class TrackerSectionController {

    private final TrackerSectionService trackerSectionService;

      
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/tracker/{trackerId}")
    public ResponseEntity<List<TrackerSectionResponse>> getSectionsByTracker(@PathVariable Long trackerId){
        return ResponseEntity.ok(trackerSectionService.getSectionsByTracker(trackerId));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<TrackerSectionResponse> getTrackerSectionById(@PathVariable Long id){
        return ResponseEntity.ok(trackerSectionService.getTrackerSectionById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assignSectiontoTracker")
    public ResponseEntity<TrackerSectionResponse> assignSectionToTracker(@RequestBody @Valid TrackerSectionRequest request){
        TrackerSectionResponse response=trackerSectionService.assignSectionToTracker(request);
        return ResponseEntity.status(201).body(response);
        
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updatetrackersection/{id}")
    public ResponseEntity<TrackerSectionResponse> updateTrackerSection(@PathVariable Long id, @RequestBody @Valid TrackerSectionRequest request){
        TrackerSectionResponse response=trackerSectionService.updateTrackerSection(id,request);
        return ResponseEntity.ok(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("deletesectionfromtracker/{id}")
    public ResponseEntity<Void> removeSectionFromTracker(@PathVariable Long id){
        trackerSectionService.removeSectionFromTracker(id);
        return ResponseEntity.noContent().build();
    }

    
    
}
