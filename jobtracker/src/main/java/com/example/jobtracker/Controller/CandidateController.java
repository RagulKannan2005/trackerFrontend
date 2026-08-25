package com.example.jobtracker.Controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobtracker.Dto.CandidateFilterRequest;
import com.example.jobtracker.Dto.CandidatePatchRequest;
import com.example.jobtracker.Dto.CandidateRequest;
import com.example.jobtracker.Dto.CandidateResponse;
import com.example.jobtracker.Service.CandidateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateService candidateService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/candidate")
    public ResponseEntity<CandidateResponse> createCandidate(@Valid @RequestBody CandidateRequest data) {
        CandidateResponse candidateResponse = candidateService.createCandidate(data);
        return ResponseEntity.status(201).body(candidateResponse);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<CandidateResponse> getMyProfile() {
        CandidateResponse candidateResponse = candidateService.getCurrentCandidateProfile();
        return ResponseEntity.ok(candidateResponse);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CandidateResponse> getCandidateById(@PathVariable Long id) {
        CandidateResponse candidateResponse = candidateService.getCandidateById(id);
        return ResponseEntity.ok(candidateResponse);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping({"/{id}", "updatecandidate/{id}"})
    public ResponseEntity<CandidateResponse> updateCandidate(@PathVariable Long id,
            @Valid @RequestBody CandidateRequest data) {
        CandidateResponse candidateResponse = candidateService.updateCandidate(id, data);
        return ResponseEntity.ok(candidateResponse);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PatchMapping({"/{id}", "updatecandidateFields/{id}"})
    public ResponseEntity<CandidateResponse> updateCandidateFields(@PathVariable Long id,
            @Valid @RequestBody CandidatePatchRequest patchdata) {
        CandidateResponse candidateresponse = candidateService.updateCandidateFields(id, patchdata);
        return ResponseEntity.ok(candidateresponse);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping({"/{id}", "deletecandidate/{id}"})
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/v1/candidates/search?candidateName=Ragul&degree=BE&graduationYear=2026&stream=CSE&page=0&size=10
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<Page<CandidateResponse>> searchCandidate(
            CandidateFilterRequest filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "candidateName") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                candidateService.searchCandidate(filter, pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allcandidate")
    public ResponseEntity<List<CandidateResponse>> getAllCandidate(){
        return ResponseEntity.ok(candidateService.getAllCandidate());
    }

}
