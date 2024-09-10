package com.saracalihan.tahakkum.dto.oauth.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppRegister {
    @NotBlank
    @Length(max = 25)
    String name;

    @NotEmpty
    String[] scopes;

    @NotBlank
    @Length(min = 10) // http://x.x
    String redirectUrl;

    @Length(max = 250)
    String description;
    String homePage;
    String photo;
}
