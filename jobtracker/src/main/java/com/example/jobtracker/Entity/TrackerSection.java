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
    private Trackers tracker;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;


    @Column(name = "display_order",nullable = false)
    private Integer displayOrder;   
}
