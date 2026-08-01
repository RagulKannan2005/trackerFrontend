package com.example.jobtracker.Entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "trackers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Trackers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TrackerName",nullable = false,unique = true)
    private String trackerName;

    @Column(name = "Description",nullable = false)
    private String description;

    @Column(name = "active",nullable = false)
    private Boolean active;

    @Column(name = "createdAt")
    private LocalDate createdAt;

    @Column(name = "updatedAt")
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

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
