package com.example.tahakkum.repository;

import com.example.tahakkum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public boolean existsByEmail(String email);
    public boolean existsByUsername(String username);
}