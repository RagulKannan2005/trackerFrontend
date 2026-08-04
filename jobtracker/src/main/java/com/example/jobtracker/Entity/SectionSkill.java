package com.example.jobtracker.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name="sectionskill")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SectionSkill {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="trackersection_id")
    private TrackerSection trackerSection;

    @ManyToOne
    @JoinColumn(name="skill_id")
    private Skills skill;

    @Column(name="display_order",nullable = false)
    private Integer displayOrder;


    
}
