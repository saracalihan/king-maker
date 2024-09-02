package com.example.tahakkum.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringJoiner;

import org.springframework.stereotype.Service;

@Service()
public class UIBuilder {
    private static UIBuilder instance = null;
    String oauthLoginTemplate = "";
    String totpTemplate = "";
    String otpTemplate = "";

    public UIBuilder(){
        try {
            if (instance != null) {
                return;
            }
            // load templates
            oauthLoginTemplate = loadTemplate("./src/main/java/com/example/tahakkum/template/oauth.html");
            totpTemplate = loadTemplate("./src/main/java/com/example/tahakkum/template/totp.html");
            otpTemplate = loadTemplate("./src/main/java/com/example/tahakkum/template/otp.html");

            System.out.println("UIBuilder: Templates loadad.");
            instance = this;
        } catch (Exception e) {
            System.err.println("UIBuilder Error:");
            System.err.println(e);
            System.exit(-1);
        }
    }

    public static synchronized UIBuilder getInstance() {
        if (instance == null)
            instance = new UIBuilder();
        return instance;
    }

    private String loadTemplate(String path) throws FileNotFoundException {
        String data = "";
        File myObj = new File(path);
        Scanner myReader = new Scanner(myObj);
        while (myReader.hasNextLine()) {
            data += myReader.nextLine();
        }
        myReader.close();
        return data;
    }

    public String createLogin(String appName, String desc, String homePage, String photo, String[] scopes,
            String redirectUrl, String validationToken) {
        String template = instance.oauthLoginTemplate;
        StringJoiner joiner = new StringJoiner(",");
        for (String scope : scopes) {
            joiner.add(scope);
        }

        if (desc == null) {
            desc = "";
        }
        if (homePage == null) {
            homePage = "";
        }
        if (photo == null) {
            photo = "";
        }

        template = template
                .replaceAll("\\{\\{scopes\\}\\}", "\"" + joiner.toString() + "\"")
                .replaceAll("\\{\\{appName\\}\\}", appName)
                .replaceAll("\\{\\{appDesc\\}\\}", desc)
                .replaceAll("\\{\\{homePage\\}\\}", homePage)
                .replaceAll("\\{\\{image\\}\\}", photo)
                .replaceAll("\\{\\{validationToken\\}\\}", validationToken)
                .replaceAll("\\{\\{redirectUrl\\}\\}", redirectUrl);

        return template;
    }

    public String createOTP(String name, String id, String secret, String redirectUrl, String failUrl){
        return instance.otpTemplate
            .replaceAll("\\{\\{id\\}\\}", String.format("\"%s\"", id))
            .replaceAll("\\{\\{secret\\}\\}", String.format("\"%s\"", secret))
            .replaceAll("\\{\\{redirectUrl\\}\\}", String.format("\"%s\"", redirectUrl))
            .replaceAll("\\{\\{failUrl\\}\\}", String.format("\"%s\"", failUrl))
            .replaceAll("\\{\\{name\\}\\}", name);
    }

    public String createTOTP(String name, int second){
        return instance.totpTemplate
            .replaceAll("\\{\\{name\\}\\}", name)
            .replaceAll("\\{\\{second\\}\\}", String.format("%d", second));
    }
}
