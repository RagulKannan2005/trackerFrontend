package com.example.jobtracker.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.jobtracker.Entity.Trackers;

@Repository
public interface TrackerRepository extends JpaRepository<Trackers, Long>{
    

}
