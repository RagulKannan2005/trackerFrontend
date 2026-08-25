package com.example.jobtracker.ServiceImp;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jobtracker.Dto.CandidateSkillProgressRequest;
import com.example.jobtracker.Dto.CandidateSkillProgressResponse;
import com.example.jobtracker.Entity.Candidate;
import com.example.jobtracker.Entity.CandidateSkillProgress;
import com.example.jobtracker.Entity.SectionSkill;
import com.example.jobtracker.Entity.Users;
import com.example.jobtracker.Enums.Status;
import com.example.jobtracker.Exception.ResourceNotFoundException;
import com.example.jobtracker.Repository.CandidateRepository;
import com.example.jobtracker.Repository.CandidateSkillProgressRepository;
import com.example.jobtracker.Repository.SectionSkillRepository;
import com.example.jobtracker.Repository.UserRepository;
import com.example.jobtracker.Service.CandidateSkillProgressService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CandidateSkillProgressServiceImp implements CandidateSkillProgressService {

    private final CandidateSkillProgressRepository progressRepo;
    private final CandidateRepository candidateRepo;
    private final SectionSkillRepository sectionSkillRepo;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CandidateSkillProgressResponse saveOrUpdateProgress(CandidateSkillProgressRequest request) {
        Candidate candidate = candidateRepo.findById(request.getCandidateId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + request.getCandidateId()));

        validateCandidateOwnership(candidate);

        SectionSkill sectionSkill = sectionSkillRepo.findById(request.getSectionSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("SectionSkill not found with id: " + request.getSectionSkillId()));

        Status status = parseStatus(request.getStatus());

        Optional<CandidateSkillProgress> existingOpt = progressRepo.findByCandidateIdAndSectionSkillId(
                candidate.getId(), sectionSkill.getId());

        CandidateSkillProgress progress;
        if (existingOpt.isPresent()) {
            progress = existingOpt.get();
            progress.setStatus(status);
            progress.setRemarks(request.getRemarks());
        } else {
            progress = CandidateSkillProgress.builder()
                    .candidate(candidate)
                    .sectionSkill(sectionSkill)
                    .status(status)
                    .remarks(request.getRemarks())
                    .build();
        }

        CandidateSkillProgress saved = progressRepo.save(progress);
        return toDto(saved);
    }

    @Override
    public List<CandidateSkillProgressResponse> getProgressByCandidate(Long candidateId) {
        Candidate candidate = candidateRepo.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + candidateId));
        validateCandidateOwnership(candidate);

        return progressRepo.findByCandidateId(candidateId).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public CandidateSkillProgressResponse getProgressByCandidateAndSkill(Long candidateId, Long sectionSkillId) {
        Candidate candidate = candidateRepo.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + candidateId));
        validateCandidateOwnership(candidate);

        CandidateSkillProgress progress = progressRepo.findByCandidateIdAndSectionSkillId(candidateId, sectionSkillId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No progress found for candidate " + candidateId + " and section skill " + sectionSkillId));

        return toDto(progress);
    }

    @Override
    @Transactional
    public void deleteProgress(Long id) {
        CandidateSkillProgress progress = progressRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill progress not found with id: " + id));

        validateCandidateOwnership(progress.getCandidate());
        progressRepo.delete(progress);
    }

    private Users getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("User authentication required");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found with email: " + email));
    }

    private boolean isAdmin(Users user) {
        return user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    private void validateCandidateOwnership(Candidate candidate) {
        Users currentUser = getAuthenticatedUser();
        if (!isAdmin(currentUser) && (candidate.getUser() == null
                || !candidate.getUser().getId().equals(currentUser.getId()))) {
            throw new IllegalArgumentException("You are not authorized to modify or view progress for this candidate");
        }
    }

    private Status parseStatus(String statusStr) {
        if (statusStr == null || statusStr.trim().isEmpty()) {
            return Status.NOT_STARTED;
        }
        try {
            return Status.valueOf(statusStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value '" + statusStr + "'. Allowed values: NOT_STARTED, IN_PROGRESS, COMPLETED");
        }
    }

    private CandidateSkillProgressResponse toDto(CandidateSkillProgress csp) {
        return CandidateSkillProgressResponse.builder()
                .id(csp.getId())
                .candidateId(csp.getCandidate() != null ? csp.getCandidate().getId() : null)
                .sectionSkillId(csp.getSectionSkill() != null ? csp.getSectionSkill().getId() : null)
                .status(csp.getStatus() != null ? csp.getStatus().name() : null)
                .remarks(csp.getRemarks())
                .build();
    }
}
