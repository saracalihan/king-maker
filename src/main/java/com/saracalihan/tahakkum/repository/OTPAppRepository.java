package com.saracalihan.tahakkum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saracalihan.tahakkum.model.OTPApp;

public interface OTPAppRepository extends JpaRepository<OTPApp, Long>{
    public OTPApp findByClientId(String id);
}
