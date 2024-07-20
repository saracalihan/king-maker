package com.example.tahakkum.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tahakkum.model.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    Token findOneByValue(String v);
}
