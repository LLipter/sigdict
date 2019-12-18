package com.llipter.sigdict.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashPassword {

    public static byte[] getSalt() {
        //Always use a SecureRandom generator
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //Create array for salt
        byte[] salt = new byte[16];

        //Get a random salt
        sr.nextBytes(salt);
        return salt;
    }

    public static String getHashedPassword(String password, byte[] salt) {
        String hashedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            // Add password bytes to digest
            md.update(salt);
            // Get the hash's bytes
            byte[] bytes = md.digest(password.getBytes("UTF-8"));
            // Convert it to hexadecimal format
            hashedPassword = Utility.binary2hexadecimal(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }


}
