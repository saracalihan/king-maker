package com.example.tahakkum.dto.oauth.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class AppRegister {
    @NotBlank
    @Length(max = 25)
    public String name;

    @NotEmpty
    public String[] scopes;

    @NotBlank
    @Length(min = 10) // http://x.x
    public String redirectUrl;

    public String description;
    public String homePage;
    public String photo;
}
