package com.saracalihan.tahakkum.dto.authentication;

import com.saracalihan.tahakkum.model.Token;
import com.saracalihan.tahakkum.model.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginResponseDto {
    public User user;
    public Token token;
}
