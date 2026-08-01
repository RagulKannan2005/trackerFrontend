package com.example.jobtracker.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobtracker.Dto.TrackerRequest;
import com.example.jobtracker.Dto.TrackerResponse;
import com.example.jobtracker.Service.TrackerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trackers")
public class TrackerController {

    private final TrackerService trackerService;


    @PostMapping("/addtracker")
    public ResponseEntity<TrackerResponse> createTracker(@RequestBody TrackerRequest tracker){
        TrackerResponse response=trackerService.createTracker(tracker);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/gettrackers")
    public ResponseEntity<List<TrackerResponse>> getAllTrackers(){
        List<TrackerResponse> response=trackerService.getAllTrackers();
        return ResponseEntity.ok(response);
    }
    
}
