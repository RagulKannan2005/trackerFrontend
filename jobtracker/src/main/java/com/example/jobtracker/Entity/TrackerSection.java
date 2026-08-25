package com.example.jobtracker.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Table(name = "sectionTracker")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackerSection {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tracker_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Trackers tracker;

    @ManyToOne
    @JoinColumn(name = "section_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Section section;


    @Column(name = "display_order",nullable = false)
    private Integer displayOrder;  
    
    @OneToMany(mappedBy="trackerSection",cascade=CascadeType.ALL,orphanRemoval=true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<SectionSkill> sectionSkills;
}
