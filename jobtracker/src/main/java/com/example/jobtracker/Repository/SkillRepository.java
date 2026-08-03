package com.example.jobtracker.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jobtracker.Entity.Skills;

@Repository
public interface SkillRepository extends JpaRepository<Skills, Long>{    

    
}
