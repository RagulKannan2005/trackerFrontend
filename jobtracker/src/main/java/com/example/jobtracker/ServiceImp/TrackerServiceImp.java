package com.example.jobtracker.ServiceImp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.jobtracker.Dto.TrackerRequest;
import com.example.jobtracker.Dto.TrackerResponse;
import com.example.jobtracker.Entity.Trackers;
import com.example.jobtracker.Repository.TrackerRepository;
import com.example.jobtracker.Service.TrackerService;

import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrackerServiceImp implements TrackerService {

    private final TrackerRepository trackerrepo;

    @Override
    public TrackerResponse createTracker(TrackerRequest request){

        Trackers tracker1=Trackers.builder()
            .trackerName(request.getTrackerName())
            .description(request.getDescription())
            .active(request.getActive())
            .build();
        
        Trackers saved=trackerrepo.save(tracker1);
        return todto(saved);


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
