package com.example.jobtracker.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobtracker.Dto.CandidateSkillProgressRequest;
import com.example.jobtracker.Dto.CandidateSkillProgressResponse;
import com.example.jobtracker.Service.CandidateSkillProgressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/candidate-skill-progress")
@RequiredArgsConstructor
public class CandidateSkillProgressController {

    private final CandidateSkillProgressService progressService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<CandidateSkillProgressResponse> saveOrUpdateProgress(
            @Valid @RequestBody CandidateSkillProgressRequest request) {
        CandidateSkillProgressResponse response = progressService.saveOrUpdateProgress(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<CandidateSkillProgressResponse>> getProgressByCandidate(
            @PathVariable Long candidateId) {
        return ResponseEntity.ok(progressService.getProgressByCandidate(candidateId));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/candidate/{candidateId}/skill/{sectionSkillId}")
    public ResponseEntity<CandidateSkillProgressResponse> getProgressByCandidateAndSkill(
            @PathVariable Long candidateId,
            @PathVariable Long sectionSkillId) {
        return ResponseEntity.ok(progressService.getProgressByCandidateAndSkill(candidateId, sectionSkillId));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgress(@PathVariable Long id) {
        progressService.deleteProgress(id);
        return ResponseEntity.noContent().build();
    }
}
