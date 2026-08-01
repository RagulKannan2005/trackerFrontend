package com.example.jobtracker.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.jobtracker.Entity.Candidate;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate> {
    Optional<Candidate> findByUserId(Long userId);
    Optional<Candidate> findByUserEmail(String email);
    boolean existsByUserId(Long userId);
    boolean existsByPhone(String phone);
}
