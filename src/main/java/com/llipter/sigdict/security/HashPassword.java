package com.llipter.sigdict.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

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
            // PBKDF2
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] bytes = factory.generateSecret(spec).getEncoded();
            hashedPassword = Utility.binary2hexadecimal(bytes);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }


}
