package com.example.jobtracker.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jobtracker.Entity.CandidateSkillProgress;

@Repository
public interface CandidateSkillProgressRepository extends JpaRepository<CandidateSkillProgress, Long> {
    Optional<CandidateSkillProgress> findByCandidateIdAndSectionSkillId(Long candidateId, Long sectionSkillId);
    List<CandidateSkillProgress> findByCandidateId(Long candidateId);
}
