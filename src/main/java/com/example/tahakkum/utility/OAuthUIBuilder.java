package com.example.tahakkum.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringJoiner;

import org.springframework.stereotype.Service;

@Service()
public class OAuthUIBuilder {
    static String loginTemplate="";

    public OAuthUIBuilder() throws FileNotFoundException {
            File myObj = new File("./src/main/java/com/example/tahakkum/template/oauth.html");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                loginTemplate += myReader.nextLine();
            }
            myReader.close();
    }

    public String createLogin(String appName, String desc, String homePage, String photo, String[] scopes, String redirectUrl, String validationToken) {
        String template = loginTemplate;
        StringJoiner joiner = new StringJoiner(",");
        for (String scope : scopes) {
            joiner.add(scope);
        }

        if(desc==null){
            desc="";
        }
        if(homePage==null){
            homePage="";
        }
        if(photo==null){
            photo="";
        }

        template = template
            .replaceAll("__scopes__", "\""+joiner.toString()+"\"")
            .replaceAll("__appName__", appName)
            .replaceAll("__appDesc__", desc)
            .replaceAll("__homePage__", homePage)
            .replaceAll("__image__", photo)
            .replaceAll("__validationToken__", validationToken)
            .replaceAll("__redirectUrl__", redirectUrl);

        return template;
    }
}
