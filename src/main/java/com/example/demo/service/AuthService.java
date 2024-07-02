package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.repository.TokenRepository;
import com.example.demo.repository.UserRepository;

@Service()
public class AuthService {
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    UserRepository userRepository;

    public AuthService(TokenRepository tokenRepository, UserRepository userRepository){
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    // public 
}
