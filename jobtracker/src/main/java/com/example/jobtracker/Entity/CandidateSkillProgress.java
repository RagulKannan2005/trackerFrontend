package com.example.jobtracker.Entity;

import java.time.LocalDate;

import com.example.jobtracker.Enums.Level;
import com.example.jobtracker.Enums.Status;
import com.example.jobtracker.Enums.YN;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "candidateskillprogress",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"candidate_id", "section_skill_id"})
    }
)
public class CandidateSkillProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_skill_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SectionSkill sectionSkill;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

   
    @Column(name = "remarks")
    private String remarks;

    @Column(name = "updatedAt")
    private LocalDate updatedAt;

    @PrePersist
    @PreUpdate
    public void onSaveOrUpdate() {
        this.updatedAt = LocalDate.now();
    }
}
