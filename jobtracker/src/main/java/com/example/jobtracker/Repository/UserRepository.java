package com.example.jobtracker.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.jobtracker.Entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    
    Optional<Users> findByEmail(String email);
    
    @Query("Select u from Users u where u.role = :role")
    List<Users> findByRole(String role);
}
