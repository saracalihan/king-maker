package com.example.tahakkum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tahakkum.model.OTPApp;

public interface OTPAppRepository extends JpaRepository<OTPApp, Long>{
    public OTPApp findByClientId(String id);
}
