package com.example.tahakkum.dto.authentication;

import com.example.tahakkum.model.Token;
import com.example.tahakkum.model.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginResponseDto {
    public User user;
    public Token token;
}
