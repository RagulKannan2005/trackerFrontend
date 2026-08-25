package com.example.jobtracker.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jobtracker.Dto.SectionSkillRequest;
import com.example.jobtracker.Dto.SectionSkillResponse;
import com.example.jobtracker.Service.SectionSkillService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/section-skills")
@RequiredArgsConstructor
public class SectionSkillController {

    private final SectionSkillService sectionSkillService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<SectionSkillResponse>> getSectionSkills() {
        return ResponseEntity.ok(sectionSkillService.getSectionSkills());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<SectionSkillResponse> createSectionSkill(@RequestBody SectionSkillRequest request) {
        return ResponseEntity.ok(sectionSkillService.createSectionSkill(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<SectionSkillResponse> updateSectionSkill(@PathVariable Long id,
            @RequestBody SectionSkillRequest request) {
        return ResponseEntity.ok(sectionSkillService.updateSectionSkill(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSectionSkill(@PathVariable Long id) {
        sectionSkillService.deleteSectionSkill(id);
        return ResponseEntity.noContent().build();
    }

}
