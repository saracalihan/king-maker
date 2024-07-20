package com.example.tahakkum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tahakkum.model.OAuthApp;

public interface OAuthAppRepository extends JpaRepository<OAuthApp, Long> {
    public boolean existsByName(String name);
    public OAuthApp findOneByClientId(String id);
}
