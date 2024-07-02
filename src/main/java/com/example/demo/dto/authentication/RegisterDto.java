package com.example.demo.dto.authentication;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.*;

public class RegisterDto {
    @NotBlank()
    public String name;

    @NotBlank()
    public String username;

    @Email()
    public String email;

    @Length(min=8, max = 25)
    public String password;

    @Length(min=8, max = 25)
    public String passwordAgain;

    public String role;
}
