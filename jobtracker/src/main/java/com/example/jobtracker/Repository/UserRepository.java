package com.example.jobtracker.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jobtracker.Entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    
    Optional<Users> findByEmail(String email);
}
