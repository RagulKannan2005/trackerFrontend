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

import com.example.jobtracker.Service.SectionService;
import com.example.jobtracker.Dto.SectionRequest;
import com.example.jobtracker.Dto.SectionResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/section")
public class SectionController {
    private final SectionService sectionService;
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/allsection")
    public ResponseEntity<List<SectionResponse>> getallSection(){
        return ResponseEntity.ok(sectionService.getallSection());
        
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addSection")
    public ResponseEntity<SectionResponse> addsection(@RequestBody SectionRequest sec){
        SectionResponse secResponse=sectionService.createSection(sec);
        return ResponseEntity.status(201).body(secResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateSection/{id}")
    public ResponseEntity<SectionResponse> updateSection(@PathVariable Long id, @RequestBody SectionRequest sec){
        SectionResponse secResponse=sectionService.updateSection(id, sec);
        return ResponseEntity.ok(secResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteSection/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable Long id){
        sectionService.deleteSection(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getSection/{id}")
    public ResponseEntity<SectionResponse> getSectionById(@PathVariable Long id){
        return ResponseEntity.ok(sectionService.getSectionById(id));
    }

    


    
}
