package com.saracalihan.tahakkum.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service()
public class Common {
    @Value("${BASE_URL}")
    private String BASE_URL;

    public String generateOTPValidateUrl(String id){
        return String.format("%s/otp/verify/%s", BASE_URL, id);
    }

    public String generateTOTPValidateUrl(String id){
        return String.format("%s/otp/time-based/verify/%s", BASE_URL, id);
    }
}
