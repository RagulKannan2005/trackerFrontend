package com.example.jobtracker.Entity;

import java.time.LocalDate;

import com.example.jobtracker.Entity.Users;

import com.example.jobtracker.Enums.English;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "candidate")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "candidateName", nullable = false)
    private String candidateName;

    @Column(name = "degree", nullable = false)
    private String degree;

    @Column(name = "Stream", nullable = false)
    private String stream;

    @Column(name = "graduationYear", nullable = false)
    private String graduationYear;

    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @Column(name = "Laptop", nullable = false)
    private Boolean haveLaptop;

    @Column(name = "internet", nullable = false)
    private Boolean haveInternet;

    @Column(name = "mobile", nullable = false)
    private Boolean haveMobile;

    @Enumerated(EnumType.STRING)
    @Column(name = "EnglishSpeaking", nullable = false)
    private English englishSpeaking;

    @Enumerated(EnumType.STRING)
    @Column(name = "englishWriting", nullable = false)
    private English englishWriting;

    @Enumerated(EnumType.STRING)
    @Column(name = "englishReading", nullable = false)
    private English englishReading;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private Users user;

    @Column(name = "createdAt", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDate updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tracker_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Trackers tracker;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDate.now();
    }

}
