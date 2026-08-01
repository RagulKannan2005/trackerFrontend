package com.example.jobtracker.ServiceImp;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.jobtracker.Dto.CandidateFilterRequest;
import com.example.jobtracker.Dto.CandidatePatchRequest;
import com.example.jobtracker.Dto.CandidateRequest;
import com.example.jobtracker.Dto.CandidateResponse;
import com.example.jobtracker.Entity.Candidate;
import com.example.jobtracker.Entity.Users;
import com.example.jobtracker.Enums.English;
import com.example.jobtracker.Exception.ResourceNotFoundException;
import com.example.jobtracker.Repository.CandidateRepository;
import com.example.jobtracker.Repository.UserRepository;
import com.example.jobtracker.Service.CandidateService;
import com.example.jobtracker.Spesification.CandidateSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CandidateServiceImp implements CandidateService {

        private final CandidateRepository candidaterepo;
        private final UserRepository userRepository;

        @Override
        public CandidateResponse createCandidate(CandidateRequest data) {
                Users currentUser = getAuthenticatedUser();
                
                Long targetUserId = data.getUserId() != null ? data.getUserId() : currentUser.getId();
                
                // Ensure non-admin users can only create profile for themselves
                if (!isAdmin(currentUser) && !targetUserId.equals(currentUser.getId())) {
                        throw new IllegalArgumentException("You can only create a candidate profile for yourself");
                }

                Users user = userRepository.findById(targetUserId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + targetUserId));

                if (candidaterepo.existsByUserId(user.getId())) {
                        throw new IllegalArgumentException("Candidate profile already exists for user ID: " + user.getId());
                }

                if (candidaterepo.existsByPhone(data.getPhone())) {
                        throw new IllegalArgumentException("Candidate with phone number " + data.getPhone() + " already exists");
                }

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
                                .orElseThrow(() -> new ResourceNotFoundException("No candidate profile found for current user"));
                return toDto(candidate);
        }

        @Override
        public CandidateResponse updateCandidate(Long id, CandidateRequest data) {
                Candidate candidate = candidaterepo.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));

                validateCandidateOwnership(candidate);

                if (!Objects.equals(candidate.getPhone(), data.getPhone()) && candidaterepo.existsByPhone(data.getPhone())) {
                        throw new IllegalArgumentException("Candidate with phone number " + data.getPhone() + " already exists");
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

                Candidate savedCandidate = candidaterepo.save(candidate);
                return toDto(savedCandidate);
        }

        @Override
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
                        if (!Objects.equals(candidate.getPhone(), request.getPhone()) && candidaterepo.existsByPhone(request.getPhone())) {
                                throw new IllegalArgumentException("Candidate with phone number " + request.getPhone() + " already exists");
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
                Candidate savedCandidate = candidaterepo.save(candidate);
                return toDto(savedCandidate);
        }

        @Override
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
                String email = authentication.getName();
                return userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found with email: " + email));
        }

        private boolean isAdmin(Users user) {
                return user.getAuthorities().stream()
                                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        }

        private void validateCandidateOwnership(Candidate candidate) {
                Users currentUser = getAuthenticatedUser();
                if (!isAdmin(currentUser) && (candidate.getUser() == null || !candidate.getUser().getId().equals(currentUser.getId()))) {
                        throw new IllegalArgumentException("You are not authorized to access or modify this candidate profile");
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

        private CandidateResponse toDto(Candidate c) {
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
                                .userId(c.getUser() != null ? c.getUser().getId() : null)
                                .username(c.getUser() != null ? c.getUser().getRealUsername() : null)
                                .build();
        }

}
