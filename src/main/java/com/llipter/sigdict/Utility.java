package com.llipter.sigdict;

import org.springframework.ui.Model;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Utility {
    private Utility() {
    }

    public static String binary2base64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] base642binary(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    public static byte[] getRandomBytes(int size) {
        //Always use a SecureRandom generator
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] bytes = new byte[size];
        sr.nextBytes(bytes);
        return bytes;
    }

    public static void addErrorMessage(Model model, String errorMessage){
        model.addAttribute("has_error", true);
        model.addAttribute("error_msg", errorMessage);
    }
}
