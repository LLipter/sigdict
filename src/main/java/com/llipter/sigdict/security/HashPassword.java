package com.llipter.sigdict.security;

import com.llipter.sigdict.Utility;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class HashPassword {

    public static final int SALT_SIZE = 16;
    public static final int PBKDF2_ITERATION_COUNT = 65536;
    public static final int PBKDF2_KEY_SIZE = 256;

    public static byte[] getSalt() {
        return Utility.getRandomBytes(SALT_SIZE);
    }

    public static String getHashedPassword(String password, byte[] salt) {
        String hashedPassword = null;
        try {
            // PBKDF2
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, PBKDF2_ITERATION_COUNT, PBKDF2_KEY_SIZE);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] bytes = factory.generateSecret(spec).getEncoded();
            hashedPassword = Utility.binary2base64(bytes);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }


}
