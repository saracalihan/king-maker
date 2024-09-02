package com.example.tahakkum.dto.otp.response;

import java.time.LocalDateTime;

public class TotpDto {
    public String id;
    public String secret;
    public String validateUrl;
    public LocalDateTime expireAt;
}
