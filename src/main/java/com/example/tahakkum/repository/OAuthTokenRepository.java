package com.example.tahakkum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tahakkum.model.OAuthToken;

public interface OAuthTokenRepository extends JpaRepository<OAuthToken, Long> {
    OAuthToken findOneByValue(String v);
}
