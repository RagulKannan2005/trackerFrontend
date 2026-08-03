package com.example.jobtracker.ServiceImp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.jobtracker.Dto.SectionRequest;
import com.example.jobtracker.Dto.SectionResponse;
import com.example.jobtracker.Entity.Section;
import com.example.jobtracker.Repository.SectionRepository;
import com.example.jobtracker.Service.SectionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionServiceImp implements SectionService {

    private final SectionRepository sectionrepo;

    @Override
    public List<SectionResponse> getallSection() {
        return sectionrepo.findAll().stream().map(this::todto).toList();
    }

    @Override
    public SectionResponse createSection(SectionRequest sec) {
        Section section = Section.builder()
                .sectionName(sec.getSectionName())
                .description(sec.getDescription())
                .active(sec.getActive())
                .build();
        Section savedSection = sectionrepo.save(section);
        return todto(savedSection);
    }

    @Override
    public SectionResponse updateSection(Long id, SectionRequest sec) {
        Section section = sectionrepo.findById(id).orElseThrow(() -> new RuntimeException("Section not found"));
        section.setSectionName(sec.getSectionName());
        section.setDescription(sec.getDescription());
        section.setActive(sec.getActive());
        Section savedSection = sectionrepo.save(section);
        return todto(savedSection);
    }

    @Override
    public void deleteSection(Long id) {
        sectionrepo.findById(id).orElseThrow(() -> new RuntimeException("Section not found"));
        sectionrepo.deleteById(id);
    }

    SectionResponse todto(Section s) {
        return SectionResponse.builder()
                .id(s.getId())
                .sectionName(s.getSectionName())
                .description(s.getDescription())
                .active(s.getActive())
                .build();
    }

}
