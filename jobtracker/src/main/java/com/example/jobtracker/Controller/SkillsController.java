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

import com.example.jobtracker.Dto.SkillsRequest;
import com.example.jobtracker.Dto.SkillsResponse;
import com.example.jobtracker.Service.SkillsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/skills")
public class SkillsController {

    private final SkillsService skillsService;

    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allSkills")
    public ResponseEntity<List<SkillsResponse>> getAllSkills(){
        List<SkillsResponse> skills = skillsService.getAllSkills();
        return ResponseEntity.ok(skills);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getSkill/{id}")
    public ResponseEntity<SkillsResponse> getSkillById(@PathVariable Long id){
        SkillsResponse skill = skillsService.getSkillById(id);
        return ResponseEntity.ok(skill);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addSkill")
    public ResponseEntity<SkillsResponse> addskill(@RequestBody @Valid SkillsRequest s){
        SkillsResponse skill = skillsService.createSkill(s);
        return ResponseEntity.ok(skill);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateSkill/{id}")
    public ResponseEntity<SkillsResponse> updateSkill(@PathVariable Long id, @RequestBody @Valid SkillsRequest s){
        SkillsResponse skill = skillsService.updateSkill(id, s);
        return ResponseEntity.ok(skill);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteSkill/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id){
        skillsService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }
    
    
}
