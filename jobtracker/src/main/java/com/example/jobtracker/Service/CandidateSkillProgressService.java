package com.example.jobtracker.Service;

import java.util.List;

import com.example.jobtracker.Dto.CandidateSkillProgressRequest;
import com.example.jobtracker.Dto.CandidateSkillProgressResponse;

public interface CandidateSkillProgressService {
    CandidateSkillProgressResponse saveOrUpdateProgress(CandidateSkillProgressRequest request);
    List<CandidateSkillProgressResponse> getProgressByCandidate(Long candidateId);
    CandidateSkillProgressResponse getProgressByCandidateAndSkill(Long candidateId, Long sectionSkillId);
    void deleteProgress(Long id);
}
