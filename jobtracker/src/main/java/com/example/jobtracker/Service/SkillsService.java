package com.example.jobtracker.Service;

import com.example.jobtracker.Dto.SkillsRequest;
import com.example.jobtracker.Dto.SkillsResponse;
import java.util.List;

public interface SkillsService {
    public List<SkillsResponse> getAllSkills();
    public SkillsResponse getSkillById(Long id);
    public SkillsResponse createSkill(SkillsRequest skillsRequest);
    public SkillsResponse updateSkill(Long id, SkillsRequest skillsRequest);
    public void deleteSkill(Long id);
}
