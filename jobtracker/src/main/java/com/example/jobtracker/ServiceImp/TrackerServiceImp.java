package com.example.jobtracker.ServiceImp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.jobtracker.Dto.TrackerRequest;
import com.example.jobtracker.Dto.TrackerResponse;
import com.example.jobtracker.Entity.Trackers;
import com.example.jobtracker.Entity.Users;
import com.example.jobtracker.Repository.TrackerRepository;
import com.example.jobtracker.Repository.UserRepository;
import com.example.jobtracker.Service.TrackerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrackerServiceImp implements TrackerService {

    private final TrackerRepository trackerrepo;
    private final UserRepository userrepo;


    private Users getEffectiveUser(){

        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        Users currentUser = userrepo.findByEmail(username)
                .or(() -> userrepo.findByUsername(username))
                .orElseThrow(() -> new RuntimeException("current user not found"));
        return currentUser;
    }

    @Override
    public TrackerResponse createTracker(TrackerRequest request){

        Users effectiveUser = getEffectiveUser();

        Trackers tracker1 = Trackers.builder()
            .trackerName(request.getTrackerName())
            .description(request.getDescription())
            .active(request.getActive())
            .user(effectiveUser)
            .build();
        
        Trackers saved = trackerrepo.save(tracker1);
        return todto(saved);
    }


    @Override
    public TrackerResponse deleteTracker(Long id){
        Trackers tracker=trackerrepo.findById(id).orElseThrow(()->new RuntimeException("Tracker not found"));
        trackerrepo.delete(tracker);
        return todto(tracker);
    }

    @Override
    public TrackerResponse updateTracker(Long id,TrackerRequest tracker){
        Trackers t=trackerrepo.findById(id).orElseThrow(()->new RuntimeException("Tracker not found"));
        t.setTrackerName(tracker.getTrackerName());
        t.setDescription(tracker.getDescription());
        t.setActive(tracker.getActive());
        Trackers updated=trackerrepo.save(t);
        return todto(updated);
    }


    @Override
    public List<TrackerResponse> getAllTrackers(){
        List<Trackers> tr=trackerrepo.findAll();
        return tr.stream().map(this::todto).collect(Collectors.toList());
    }

    TrackerResponse todto(Trackers t){
        return TrackerResponse.builder()
            .id(t.getId())
            .trackerName(t.getTrackerName())
            .description(t.getDescription())
            .active(t.getActive())
            .build();
    }



    
}
