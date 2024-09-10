package com.saracalihan.tahakkum.dto.otp.request;

import java.util.HashMap;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterAppDto {
    @NotBlank
    String name;

    @NotBlank
    @Length(min = 10) // http://x.x
    String redirectUrl;

    @NotBlank
    @Length(min = 10) // http://x.x
    String failRedirectUrl;

    @NotBlank
    @Length(min = 10) // http://x.x
    String codeRedirectUrl;
    @NotBlank
    @Length(min = 3, max=6) // GET, POST, PUT, DELETE
    String codeRedirectMethod;
    HashMap<String, String> codeRedirectHeader;
    HashMap<String, String> codeRedirectBody;
    HashMap<String, String> codeRedirectQueries;
}
