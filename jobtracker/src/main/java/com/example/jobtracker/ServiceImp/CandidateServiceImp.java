package com.example.jobtracker.ServiceImp;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.jobtracker.Dto.CandidateFilterRequest;
import com.example.jobtracker.Dto.CandidatePatchRequest;
import com.example.jobtracker.Dto.CandidateRequest;
import com.example.jobtracker.Dto.CandidateResponse;
import com.example.jobtracker.Dto.SectionDetailResponse;
import com.example.jobtracker.Dto.SkillDetailResponse;
import com.example.jobtracker.Dto.TrackerDetailResponse;
import com.example.jobtracker.Entity.Candidate;
import com.example.jobtracker.Entity.CandidateSkillProgress;
import com.example.jobtracker.Entity.Section;
import com.example.jobtracker.Entity.Skills;
import com.example.jobtracker.Entity.Trackers;
import com.example.jobtracker.Entity.Users;
import com.example.jobtracker.Enums.English;
import com.example.jobtracker.Exception.ResourceNotFoundException;
import com.example.jobtracker.Repository.CandidateRepository;
import com.example.jobtracker.Repository.TrackerRepository;
import com.example.jobtracker.Repository.UserRepository;
import com.example.jobtracker.Service.CandidateService;
import com.example.jobtracker.Spesification.CandidateSpecification;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CandidateServiceImp implements CandidateService {

        private final CandidateRepository candidaterepo;
        private final UserRepository userRepository;
        private final TrackerRepository trackerRepository;
        @Override
        @Transactional
        public CandidateResponse createCandidate(CandidateRequest data) {
                Users currentUser = getAuthenticatedUser();

                Long targetUserId = data.getUserId() != null ? data.getUserId() : currentUser.getId();

                // Ensure non-admin users can only create profile for themselves
                if (!isAdmin(currentUser) && !targetUserId.equals(currentUser.getId())) {
                        throw new IllegalArgumentException("You can only create a candidate profile for yourself");
                }

                Users user = userRepository.findById(targetUserId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "User not found with id: " + targetUserId));

                if (candidaterepo.existsByUserId(user.getId())) {
                        throw new IllegalArgumentException(
                                        "Candidate profile already exists for user ID: " + user.getId());
                }

                if (candidaterepo.existsByPhone(data.getPhone())) {
                        throw new IllegalArgumentException(
                                        "Candidate with phone number " + data.getPhone() + " already exists");
                }

                Trackers tracker = trackerRepository.findById(data.getTrackerId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Tracker not found with id: " + data.getTrackerId()));

                Candidate candidate = Candidate.builder()
                                .candidateName(data.getCandidateName())
                                .degree(data.getDegree())
                                .stream(data.getStream())
                                .graduationYear(data.getGraduationYear())
                                .phone(data.getPhone())
                                .haveLaptop(data.getHaveLaptop())
                                .haveInternet(data.getHaveInternet())
                                .haveMobile(data.getHaveMobile())
                                .englishSpeaking(parseEnglishEnum(data.getEnglishSpeaking(), "englishSpeaking"))
                                .englishWriting(parseEnglishEnum(data.getEnglishWriting(), "englishWriting"))
                                .englishReading(parseEnglishEnum(data.getEnglishReading(), "englishReading"))
                                .user(user)
                                .tracker(tracker)
                                .build();

                Candidate savedCandidate = candidaterepo.save(candidate);
                return toDto(savedCandidate);
        }

        @Override
        public CandidateResponse getCandidateById(Long id) {
                Candidate candidate = candidaterepo.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
                validateCandidateOwnership(candidate);
                return toDto(candidate);
        }

        @Override
        public CandidateResponse getCurrentCandidateProfile() {
                Users currentUser = getAuthenticatedUser();
                Candidate candidate = candidaterepo.findByUserId(currentUser.getId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "No candidate profile found for current user"));
                return toDto(candidate);
        }

        @Override
        @Transactional
        public CandidateResponse updateCandidate(Long id, CandidateRequest data) {
                Candidate candidate = candidaterepo.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));

                validateCandidateOwnership(candidate);

                if (!Objects.equals(candidate.getPhone(), data.getPhone())
                                && candidaterepo.existsByPhone(data.getPhone())) {
                        throw new IllegalArgumentException(
                                        "Candidate with phone number " + data.getPhone() + " already exists");
                }

                candidate.setCandidateName(data.getCandidateName());
                candidate.setDegree(data.getDegree());
                candidate.setStream(data.getStream());
                candidate.setGraduationYear(data.getGraduationYear());
                candidate.setPhone(data.getPhone());
                candidate.setHaveLaptop(data.getHaveLaptop());
                candidate.setHaveInternet(data.getHaveInternet());
                candidate.setHaveMobile(data.getHaveMobile());
                candidate.setEnglishSpeaking(parseEnglishEnum(data.getEnglishSpeaking(), "englishSpeaking"));
                candidate.setEnglishWriting(parseEnglishEnum(data.getEnglishWriting(), "englishWriting"));
                candidate.setEnglishReading(parseEnglishEnum(data.getEnglishReading(), "englishReading"));

                if (data.getTrackerId() != null) {
                        Trackers tracker = trackerRepository.findById(data.getTrackerId())
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Tracker not found with id: " + data.getTrackerId()));
                        candidate.setTracker(tracker);
                }

                Candidate savedCandidate = candidaterepo.save(candidate);
                return toDto(savedCandidate);
        }

        @Override
        @Transactional
        public CandidateResponse updateCandidateFields(Long id, CandidatePatchRequest request) {
                Candidate candidate = candidaterepo.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));

                validateCandidateOwnership(candidate);

                if (request.getCandidateName() != null) {
                        candidate.setCandidateName(request.getCandidateName());
                }
                if (request.getDegree() != null) {
                        candidate.setDegree(request.getDegree());
                }
                if (request.getStream() != null) {
                        candidate.setStream(request.getStream());
                }
                if (request.getGraduationYear() != null) {
                        candidate.setGraduationYear(request.getGraduationYear());
                } else if (request.getGreadutionYear() != null) {
                        candidate.setGraduationYear(request.getGreadutionYear());
                }
                if (request.getPhone() != null) {
                        if (!Objects.equals(candidate.getPhone(), request.getPhone())
                                        && candidaterepo.existsByPhone(request.getPhone())) {
                                throw new IllegalArgumentException("Candidate with phone number " + request.getPhone()
                                                + " already exists");
                        }
                        candidate.setPhone(request.getPhone());
                }
                if (request.getHaveLaptop() != null) {
                        candidate.setHaveLaptop(request.getHaveLaptop());
                }
                if (request.getHaveInternet() != null) {
                        candidate.setHaveInternet(request.getHaveInternet());
                }
                if (request.getHaveMobile() != null) {
                        candidate.setHaveMobile(request.getHaveMobile());
                }
                if (request.getEnglishSpeaking() != null) {
                        candidate.setEnglishSpeaking(parseEnglishEnum(request.getEnglishSpeaking(), "englishSpeaking"));
                }
                if (request.getEnglishWriting() != null) {
                        candidate.setEnglishWriting(parseEnglishEnum(request.getEnglishWriting(), "englishWriting"));
                }
                if (request.getEnglishReading() != null) {
                        candidate.setEnglishReading(parseEnglishEnum(request.getEnglishReading(), "englishReading"));
                }
                if (request.getTrackerId() != null) {
                        Trackers tracker = trackerRepository.findById(request.getTrackerId())
                                        .orElseThrow(() -> new ResourceNotFoundException(
                                                        "Tracker not found with id: " + request.getTrackerId()));
                        candidate.setTracker(tracker);
                }
                Candidate savedCandidate = candidaterepo.save(candidate);
                return toDto(savedCandidate);
        }

        @Override
        @Transactional
        public void deleteCandidate(Long id) {
                Candidate candidate = candidaterepo.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
                validateCandidateOwnership(candidate);
                candidaterepo.delete(candidate);
        }

        @Override
        public Page<CandidateResponse> searchCandidate(
                        CandidateFilterRequest filter,
                        Pageable pageable) {

                Page<Candidate> candidates = candidaterepo.findAll(
                                CandidateSpecification.filter(filter),
                                pageable);

                return candidates.map(this::toDto);
        }

        private Users getAuthenticatedUser() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || !authentication.isAuthenticated()) {
                        throw new IllegalArgumentException("User authentication required");
                }
                String identity = authentication.getName();
                return userRepository.findByEmail(identity)
                                .or(() -> userRepository.findByUsername(identity))
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Authenticated user not found with identity: " + identity));
        }

        private boolean isAdmin(Users user) {
                return user.getAuthorities().stream()
                                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        }

        private void validateCandidateOwnership(Candidate candidate) {
                Users currentUser = getAuthenticatedUser();
                if (!isAdmin(currentUser) && (candidate.getUser() == null
                                || !candidate.getUser().getId().equals(currentUser.getId()))) {
                        throw new IllegalArgumentException(
                                        "You are not authorized to access or modify this candidate profile");
                }
        }

        private English parseEnglishEnum(String value, String fieldName) {
                if (value == null || value.trim().isEmpty()) {
                        return null;
                }
                try {
                        return English.valueOf(value.trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Invalid value '" + value + "' for " + fieldName
                                        + ". Allowed values: BASIC, INTERMEDIATE, ADVANCED, FLUENT");
                }
        }

        @Override
        public List<CandidateResponse> getAllCandidate() {
                List<Candidate> ca = candidaterepo.findAll();
                return ca.stream().map(this::toDto).toList();
        }

        private CandidateResponse toDto(Candidate c) {
                Map<Long, CandidateSkillProgress> progressMap = (c.getSkillProgresses() != null)
                                ? c.getSkillProgresses().stream()
                                                .filter(csp -> csp.getSectionSkill() != null && csp.getSectionSkill().getId() != null)
                                                .collect(Collectors.toMap(
                                                                csp -> csp.getSectionSkill().getId(),
                                                                csp -> csp,
                                                                (existing, replacement) -> replacement))
                                : Map.of();

                return CandidateResponse.builder()
                                .id(c.getId())
                                .candidateName(c.getCandidateName())
                                .degree(c.getDegree())
                                .stream(c.getStream())
                                .graduationYear(c.getGraduationYear())
                                .phone(c.getPhone())
                                .haveLaptop(c.getHaveLaptop())
                                .haveInternet(c.getHaveInternet())
                                .haveMobile(c.getHaveMobile())
                                .englishSpeaking(c.getEnglishSpeaking() != null ? c.getEnglishSpeaking().name() : null)
                                .englishWriting(c.getEnglishWriting() != null ? c.getEnglishWriting().name() : null)
                                .englishReading(c.getEnglishReading() != null ? c.getEnglishReading().name() : null)
                                .createdAt(c.getCreatedAt())
                                .updatedAt(c.getUpdatedAt())
                                .userId(c.getUser() != null ? c.getUser().getId() : null)
                                .username(c.getUser() != null ? c.getUser().getRealUsername() : null)
                                .trackerId(c.getTracker() != null ? c.getTracker().getId() : null)
                                .trackerName(c.getTracker() != null ? c.getTracker().getTrackerName() : null)
                                .trackerDetails(mapTrackerDetails(c.getTracker(), progressMap))
                                .build();
        }

        private TrackerDetailResponse mapTrackerDetails(Trackers t, Map<Long, CandidateSkillProgress> progressMap) {
                if (t == null) {
                        return null;
                }

                List<SectionDetailResponse> sectionDtos = (t.getTrackerSections() != null)
                                ? t.getTrackerSections().stream()
                                                .sorted(Comparator.comparing(ts -> ts.getDisplayOrder() != null ? ts.getDisplayOrder() : 0))
                                                .map(ts -> {
                                                        Section sec = ts.getSection();
                                                        List<SkillDetailResponse> skillDtos = (ts.getSectionSkills() != null)
                                                                        ? ts.getSectionSkills().stream()
                                                                                        .sorted(Comparator.comparing(ss -> ss.getDisplayOrder() != null ? ss.getDisplayOrder() : 0))
                                                                                        .map(ss -> {
                                                                                                Skills sk = ss.getSkill();
                                                                                                CandidateSkillProgress csp = progressMap != null ? progressMap.get(ss.getId()) : null;
                                                                                                return SkillDetailResponse.builder()
                                                                                                                .sectionSkillId(ss.getId())
                                                                                                                .skillId(sk != null ? sk.getId() : null)
                                                                                                                .skillName(sk != null ? sk.getSkillName() : null)
                                                                                                                .description(sk != null ? sk.getDescription() : null)
                                                                                                                .displayOrder(ss.getDisplayOrder())
                                                                                                                .progressId(csp != null ? csp.getId() : null)
                                                                                                                .status(csp != null && csp.getStatus() != null ? csp.getStatus().name() : "NOT_STARTED")
                                                                                                                .remarks(csp != null ? csp.getRemarks() : null)
                                                                                                                .progressUpdatedAt(csp != null ? csp.getUpdatedAt() : null)
                                                                                                                .build();
                                                                                        })
                                                                                        .toList()
                                                                        : List.of();

                                                        return SectionDetailResponse.builder()
                                                                        .trackerSectionId(ts.getId())
                                                                        .sectionId(sec != null ? sec.getId() : null)
                                                                        .sectionName(sec != null ? sec.getSectionName() : null)
                                                                        .description(sec != null ? sec.getDescription() : null)
                                                                        .displayOrder(ts.getDisplayOrder())
                                                                        .skills(skillDtos)
                                                                        .build();
                                                })
                                                .toList()
                                : List.of();

                return TrackerDetailResponse.builder()
                                .id(t.getId())
                                .trackerName(t.getTrackerName())
                                .description(t.getDescription())
                                .active(t.getActive())
                                .sections(sectionDtos)
                                .build();
        }


}
