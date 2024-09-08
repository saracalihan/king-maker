package com.example.tahakkum.service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tahakkum.constant.Constants;
import com.example.tahakkum.constant.OTPStatus;
import com.example.tahakkum.constant.OTPTypes;
import com.example.tahakkum.constant.OTPVariables;
import com.example.tahakkum.dto.otp.request.RegisterAppDto;
import com.example.tahakkum.model.OTPApp;
import com.example.tahakkum.model.OTPCode;
import com.example.tahakkum.model.User;
import com.example.tahakkum.repository.OTPAppRepository;
import com.example.tahakkum.repository.OTPCodeRepository;
import com.example.tahakkum.utility.Common;
import com.example.tahakkum.utility.Cryptation;

@Service
public class OTPService {
    @Autowired
    private OTPCodeRepository otpCodeRepository;

    @Autowired
    private OTPAppRepository appRepository;

    @Autowired
    private Common common;

    /**
     * 
     * @return random 6 digit intager
     */
    private int generateCode() {
        Random r = new Random();
        return r.nextInt(999999 - 100000) + 100000;
    }

    private int generateOTPCode() {
        int code;
        do {
            code = generateCode();
            OTPCode c = otpCodeRepository.findByCode(code);
            if (c != null && (c.getType() == OTPTypes.OTP || c.getStatus().equals(OTPStatus.Active.toString()))) {
                continue;
            }
            break;
        } while (true);
        return code;
    }

    private int generateTOTPCode() {
        int code;
        do {
            code = generateCode();
            OTPCode c = otpCodeRepository.findByCode(code);
            if (c != null && (c.getType() == OTPTypes.TOTP || c.getStatus().equals(OTPStatus.Active.toString()))) {
                continue;
            }
            break;
        } while (true);
        return code;
    }

    public OTPApp createApp(RegisterAppDto data, User user) {
        OTPApp app = new OTPApp();

        app.setName(data.getName());
        app.setRedirectUrl(data.getRedirectUrl());
        app.setFailRedirectUrl(data.getFailRedirectUrl());
        app.setClientId(Cryptation.generateUrlSafeToken(24));
        app.setClientSecret(Cryptation.generateUrlSafeToken(24));
        app.setOwner(user);

        app.setCodeRedirectUrl(data.getCodeRedirectUrl());
        app.setCodeRedirectBody(data.getCodeRedirectBody());
        app.setCodeRedirectHeader(data.getCodeRedirectHeader());
        app.setCodeRedirectMethod(data.getCodeRedirectMethod());
        // app.setCodeRedirectQueries(data.getCodeRedirectQueries());

        appRepository.save(app);
        return app;
    }

    public OTPApp findApp(String id, String secret) {
        OTPApp app = appRepository.findByClientId(id);
        if (app == null) {
            return null;
        }
        if (!secret.equals(app.getClientSecret())) {
            return null;
        }
        return app;
    }

    public OTPCode createOtpCode(OTPApp app, HashMap<String, Object> metadata) {
        OTPCode code = new OTPCode();
        code.setApp(app);
        code.setCode(generateOTPCode());
        code.setMetadatas(metadata);
        code.setType(OTPTypes.OTP);
        code.setOtpId(Cryptation.generateUrlSafeToken(24));
        code.setOtpSecret(Cryptation.generateUrlSafeToken(24));
        code.setExpiredAt(LocalDateTime.now().plusDays(3));
        otpCodeRepository.save(code);
        return code;
    }

    public OTPApp replaceOtpCodeVariables(OTPApp app, OTPCode code) {
        try {
            for (OTPVariables var : OTPVariables.values()) {
                String varName = var.toString(), propertyName = "";
                switch (varName) {
                    case "__id__":
                        propertyName = "otpId";
                        break;
                    case "__code__":
                        propertyName = "code";
                        break;
                    case "__secret__":
                        propertyName = "otpSecret";
                        break;
                    case "__verifyUrl__":
                        continue;
                }

                Field field = OTPCode.class.getDeclaredField(propertyName);
                field.setAccessible(true);
                String varValue = propertyName.equals("code")
                        ? String.format("%d", field.getInt(code))
                        : (String) field.get(code);
                field.setAccessible(false);

                app.setCodeRedirectUrl(
                        app.getCodeRedirectUrl()
                                .replaceAll(
                                        varName,
                                        varValue));

                for (Entry<String, String> entry : app.getCodeRedirectHeader().entrySet()) {
                    String value = entry.getValue();
                    String newValue = value.replaceAll(varName, varValue);
                    app.getCodeRedirectHeader().put(entry.getKey(), newValue);
                }

                // TODO: app.getCodeRedirectBody()
            }

            // __verifyUrl__
            String verifyKey = OTPVariables.VerifyUrl.toString(),
                    verifyValue = common.generateOTPValidateUrl(code.getOtpId());

            app.setCodeRedirectUrl(
                app.getCodeRedirectUrl()
                    .replaceAll(verifyKey,verifyValue));

            for (Entry<String, String> entry : app.getCodeRedirectHeader().entrySet()) {
                String value = entry.getValue();
                String newValue = value.replaceAll(verifyKey, value);
                app.getCodeRedirectHeader().put(entry.getKey(), newValue);
            }
            return app;
        } catch (

        Exception e) {
            System.err.println(e);
            return null;
        }
    }

    public OTPCode createTotpCode(OTPApp app, int time, HashMap<String, Object> metadata) {
        OTPCode code = new OTPCode();
        code.setApp(app);
        code.setCode(generateOTPCode());
        code.setMetadatas(metadata);
        code.setType(OTPTypes.TOTP);
        code.setOtpId(Cryptation.generateUrlSafeToken(24));
        code.setOtpSecret(Cryptation.generateUrlSafeToken(24));
        System.err.println("time");
        System.err.println(time);
        code.setExpiredAt(LocalDateTime.now().plusSeconds((long)(time + Constants.TOPT_TIME_PADDING)));
        otpCodeRepository.save(code);
        return code;
    }

    public void deleteCode(OTPCode code) {
        otpCodeRepository.delete(code);
    }

    public OTPCode findCodeByOtpId(String id){
        return this.otpCodeRepository.findByOtpId(id);
    }

    public OTPCodeRepository getOTPCodeRepository(){
        return this.otpCodeRepository;
    }
}
