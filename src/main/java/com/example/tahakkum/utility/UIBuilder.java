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

    public UIBuilder(){
        try {
            if (instance != null) {
                return;
            }
            // load templates
            oauthLoginTemplate = loadTemplate("./src/main/java/com/example/tahakkum/template/oauth.html");

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
}
