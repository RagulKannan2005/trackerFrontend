package com.example.jobtracker.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.jobtracker.Dto.TrackerRequest;
import com.example.jobtracker.Dto.TrackerResponse;
import com.example.jobtracker.Service.TrackerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trackers")
public class TrackerController {

    private final TrackerService trackerService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addtracker")
    public ResponseEntity<TrackerResponse> createTracker(@RequestBody TrackerRequest tracker){
        TrackerResponse response=trackerService.createTracker(tracker);
        return ResponseEntity.status(201).body(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/gettrackers")
    public ResponseEntity<List<TrackerResponse>> getAllTrackers(){
        List<TrackerResponse> response=trackerService.getAllTrackers();
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deletetracker/{id}")
    public ResponseEntity<TrackerResponse> deleteTracker(@PathVariable Long id){
        TrackerResponse response=trackerService.deleteTracker(id);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updatetracker/{id}")
    public ResponseEntity<TrackerResponse> updateTracker(@PathVariable Long id, @RequestBody TrackerRequest tracker){
        TrackerResponse response=trackerService.updateTracker(id,tracker);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/gettracker/{id}")
    public ResponseEntity<TrackerResponse> getTrackerById(@PathVariable Long id){
        TrackerResponse response = trackerService.getTrackerById(id);
        return ResponseEntity.ok(response);
    }
}
