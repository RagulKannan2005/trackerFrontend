package com.example.jobtracker.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jobtracker.Entity.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section,Long>{
    Optional<Section> findBySectionName(String sectionName);

  
}
