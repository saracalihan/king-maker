package com.example.demo.dto.authentication;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public class LoginDto{
    @NotBlank()
    String username;

    @NotBlank()
    @Length(min=8, max = 25)
    String password;
}
