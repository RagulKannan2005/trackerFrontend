package com.example.jobtracker.Service;

import java.util.List;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.jobtracker.Dto.CandidateFilterRequest;
import com.example.jobtracker.Dto.CandidatePatchRequest;
import com.example.jobtracker.Dto.CandidateRequest;
import com.example.jobtracker.Dto.CandidateResponse;

public interface CandidateService {
    CandidateResponse createCandidate(CandidateRequest data);
    List<CandidateResponse> getAllCandidate();
    CandidateResponse getCandidateById(Long id);
    CandidateResponse getCurrentCandidateProfile();
    CandidateResponse updateCandidate(Long id, CandidateRequest data);
    CandidateResponse updateCandidateFields(Long id, CandidatePatchRequest patchdata);
    void deleteCandidate(Long id);
    Page<CandidateResponse> searchCandidate(CandidateFilterRequest filter, Pageable pageable);
}
