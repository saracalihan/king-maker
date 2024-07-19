package com.example.tahakkum.utility;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class Cryptation {
    public static class HashAndSalt{
        public String hash;
        public String salt;
    }
    public static byte[] createSalt(int size){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[size];
        random.nextBytes(salt);
        return salt;
    }
    public static HashAndSalt hashAndSaltPassword(String pass){
        HashAndSalt hns = new HashAndSalt();

            // Generate a random salt
            byte[] salt = createSalt(16);

            byte[] hashedPassword = Cryptation.hashPassword(pass, salt);
            // Convert byte arrays to Base64 strings
            hns.hash = byteToString(hashedPassword);
            hns.salt = byteToString(salt);
        return hns;
    }

    public static String byteToString(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] hashPassword(String pass, byte[] salt){
        try {
          // Create MessageDigest instance for SHA-512
          MessageDigest md = MessageDigest.getInstance("SHA-512");
          md.update(salt);

          // Hash the password
          return md.digest(pass.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            System.err.println(e.toString());
            System.exit(-1);
        }

        return new byte[0];
    }

    public static boolean comparePassword(String pass, String hash, String salt){   
        byte[] sByte = Base64.getDecoder().decode(salt);
        String pHash = Base64.getEncoder().encodeToString(hashPassword(pass, sByte));
        return hash.equals(pHash);
    }
}
