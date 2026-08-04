package com.example.jobtracker.ServiceImp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.jobtracker.Dto.SectionSkillRequest;
import com.example.jobtracker.Dto.SectionSkillResponse;
import com.example.jobtracker.Entity.SectionSkill;
import com.example.jobtracker.Entity.Skills;
import com.example.jobtracker.Entity.TrackerSection;
import com.example.jobtracker.Repository.SectionSkillRepository;
import com.example.jobtracker.Repository.SkillRepository;
import com.example.jobtracker.Repository.TrackerSectionRepository;
import com.example.jobtracker.Service.SectionSkillService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionSkillServiceImp implements SectionSkillService {

    private final SectionSkillRepository sectionSkillRepo;
    private final TrackerSectionRepository trackerSectionRepo;
    private final SkillRepository skillRepo;

    @Override
    public SectionSkillResponse createSectionSkill(SectionSkillRequest request) {
        TrackerSection trackerSection = trackerSectionRepo.findById(request.getTrackerSectionId())
                .orElseThrow(() -> new RuntimeException("Tracker Section not found"));
        Skills skill = skillRepo.findById(request.getSkillId())
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        SectionSkill sectionSkill = SectionSkill.builder()
                .trackerSection(trackerSection)
                .skill(skill)
                .displayOrder(request.getDisplayOrder())
                .build();

        SectionSkill saved = sectionSkillRepo.save(sectionSkill);
        return toDto(saved);
    }

    @Override
    public SectionSkillResponse updateSectionSkill(Long id, SectionSkillRequest request) {
        SectionSkill sectionSkill = sectionSkillRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Section Skill not found"));

        TrackerSection trackerSection = trackerSectionRepo.findById(request.getTrackerSectionId())
                .orElseThrow(() -> new RuntimeException("Tracker Section not found"));
        Skills skill = skillRepo.findById(request.getSkillId())
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        sectionSkill.setTrackerSection(trackerSection);
        sectionSkill.setSkill(skill);
        sectionSkill.setDisplayOrder(request.getDisplayOrder());

        SectionSkill updated = sectionSkillRepo.save(sectionSkill);
        return toDto(updated);
    }

    @Override
    public List<SectionSkillResponse> getSectionSkills() {
        return sectionSkillRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SectionSkillResponse> getSectionSkillsBySkillId(Long skillId) {
        return sectionSkillRepo.findBySkillId(skillId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SectionSkillResponse> getSectionSkillsByTrackerSectionId(Long trackerSectionId) {
        return sectionSkillRepo.findByTrackerSectionId(trackerSectionId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SectionSkillResponse getSectionSkillById(Long id) {
        SectionSkill sectionSkill = sectionSkillRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Section Skill not found"));
        return toDto(sectionSkill);
    }

    @Override
    public void deleteSectionSkill(Long id) {
        if (!sectionSkillRepo.existsById(id)) {
            throw new RuntimeException("Section Skill not found");
        }
        sectionSkillRepo.deleteById(id);
    }

    private SectionSkillResponse toDto(SectionSkill s) {
        return SectionSkillResponse.builder()
                .id(s.getId())
                .trackerSectionId(s.getTrackerSection() != null ? s.getTrackerSection().getId() : null)
                .skillId(s.getSkill() != null ? s.getSkill().getId() : null)
                .displayOrder(s.getDisplayOrder())
                .build();
    }
}
