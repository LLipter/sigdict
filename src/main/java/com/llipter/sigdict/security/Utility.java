package com.llipter.sigdict.security;

import java.util.Base64;

public class Utility {
    public static String binary2base64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static byte[] base642binary(String base64) {
        return Base64.getDecoder().decode(base64);
    }
}
