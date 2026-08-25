package com.example.jobtracker.Service;

import java.util.List;

import com.example.jobtracker.Dto.SectionSkillRequest;
import com.example.jobtracker.Dto.SectionSkillResponse;

public interface SectionSkillService {

    SectionSkillResponse createSectionSkill(SectionSkillRequest request);
    SectionSkillResponse updateSectionSkill(Long id,SectionSkillRequest request);
    List<SectionSkillResponse> getSectionSkills();
    List<SectionSkillResponse> getSectionSkillsBySkillId(Long skillId);
    List<SectionSkillResponse> getSectionSkillsByTrackerSectionId(Long trackerSectionId);
    SectionSkillResponse getSectionSkillById(Long id);
    void deleteSectionSkill(Long id);
}
