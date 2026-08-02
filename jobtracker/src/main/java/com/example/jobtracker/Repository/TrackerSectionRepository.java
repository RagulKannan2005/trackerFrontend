package com.example.jobtracker.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jobtracker.Entity.TrackerSection;

import java.util.List;

public interface TrackerSectionRepository extends JpaRepository<TrackerSection, Long> {

    List<TrackerSection> findByTrackerId(Long trackerId);

}
