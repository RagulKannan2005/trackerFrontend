package com.example.jobtracker.Service;

import java.util.List;

import com.example.jobtracker.Dto.SectionRequest;
import com.example.jobtracker.Dto.SectionResponse;

public interface SectionService {
    SectionResponse createSection(SectionRequest sec);
    List<SectionResponse> getallSection();  
    SectionResponse updateSection(Long id,SectionRequest sec);
    void deleteSection(Long id);
    SectionResponse getSectionById(Long id);
}
