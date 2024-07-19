package com.example.tahakkum.dto.authentication;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public class LoginDto{
    @NotBlank()
    public String username;

    @NotBlank()
    @Length(min=8, max = 25)
    public String password;
}
