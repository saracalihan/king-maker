package com.example.tahakkum.repository;

import com.example.tahakkum.model.OTPCode;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OTPCodeRepository extends JpaRepository<OTPCode, Long> {
    public OTPCode findByCode(int code);
    public OTPCode findByOtpId(String i);
}
