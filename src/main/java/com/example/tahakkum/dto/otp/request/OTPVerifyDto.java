package com.example.tahakkum.dto.otp.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class OTPVerifyDto {
    @NotBlank
    public String id;

    @NotBlank
    public String secret;

    @Min(100000)
    @Max(999999)
    public Integer code;
}
