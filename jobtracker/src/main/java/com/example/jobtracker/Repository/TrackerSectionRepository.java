package com.example.jobtracker.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.jobtracker.Entity.TrackerSection;

import java.util.List;
import java.util.Optional;

public interface TrackerSectionRepository extends JpaRepository<TrackerSection, Long> {

    List<TrackerSection> findByTrackerId(Long trackerId);

    @Query("SELECT ts FROM TrackerSection ts JOIN FETCH ts.tracker JOIN FETCH ts.section WHERE ts.tracker.id = :trackerId ORDER BY ts.displayOrder ASC")
    List<TrackerSection> findByTrackerIdOrderByDisplayOrderAsc(@Param("trackerId") Long trackerId);

    Optional<TrackerSection> findByTrackerIdAndSectionId(Long trackerId, Long sectionId);

    void deleteByTrackerIdAndSectionId(Long trackerId, Long sectionId);
}
