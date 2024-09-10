package com.saracalihan.tahakkum.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.saracalihan.tahakkum.model.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {
    Token findOneByValue(String v);
}
