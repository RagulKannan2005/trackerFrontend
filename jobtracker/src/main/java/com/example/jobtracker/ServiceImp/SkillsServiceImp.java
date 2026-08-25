package com.example.jobtracker.ServiceImp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.jobtracker.Dto.SkillsRequest;
import com.example.jobtracker.Dto.SkillsResponse;
import com.example.jobtracker.Entity.Skills;
import com.example.jobtracker.Repository.SkillRepository;
import com.example.jobtracker.Service.SkillsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillsServiceImp implements SkillsService{

    private final SkillRepository skillrepo;

    @Override
    public SkillsResponse createSkill(SkillsRequest request){
        Skills skills =Skills.builder()
        .skillName(request.getSkillName())
        .description(request.getDescription())
        .active(request.getActive())
        .build();

        Skills saved = skillrepo.save(skills);
        return toDto(saved);
    }

    @Override
    public SkillsResponse getSkillById(Long id){
        Skills skills = skillrepo.findById(id).orElseThrow();
        return toDto(skills);
    }

    @Override
    public SkillsResponse updateSkill(Long id, SkillsRequest request){
        Skills skills = skillrepo.findById(id).orElseThrow(()->new RuntimeException("Skill not found"));
        skills.setSkillName(request.getSkillName());
        skills.setDescription(request.getDescription());
        skills.setActive(request.getActive());
        Skills saved = skillrepo.save(skills);
        return toDto(saved);
    }

    @Override
    public void deleteSkill(Long id){
        skillrepo.deleteById(id);
    }

    @Override
    public List<SkillsResponse> getAllSkills(){
        List<Skills> skills = skillrepo.findAll();
        return skills.stream().map(this::toDto).collect(Collectors.toList());
    }



    SkillsResponse toDto(Skills s){
        return SkillsResponse.builder()
        .id(s.getId())
        .skillName(s.getSkillName())
        .description(s.getDescription())
        .active(s.getActive())
        .build();
    }
}
