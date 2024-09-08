package com.example.tahakkum.dto.otp.request;

import java.util.HashMap;

import jakarta.validation.constraints.Min;

public class TOTPCreateDto {
    @Min(1)
    public int time;
    public HashMap<String, Object> metadatas;
}
