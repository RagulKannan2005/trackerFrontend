package com.example.jobtracker.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jobtracker.Entity.SectionSkill;

@Repository
public interface SectionSkillRepository extends JpaRepository<SectionSkill, Long> {
    List<SectionSkill> findBySkillId(Long skillId);
    List<SectionSkill> findByTrackerSectionId(Long trackerSectionId);
}
