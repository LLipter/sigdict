package com.llipter.sigdict.utility;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

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

    public static String timestamp2String(Timestamp timestamp) {
        Date date = new Date();
        date.setTime(timestamp.getTime());
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String getBaseUrlFromRequest(HttpServletRequest request) {
        String baseUrl = String.format(
                "%s://%s:%d",
                request.getScheme(),
                request.getServerName(),
                request.getServerPort());
        return baseUrl;
    }

    // this encoding will prevent special characters like "+=?" from being misinterpreted.
    public static String urlEncodedString(String string){
        String urlString = null;
        try {
            urlString = URLEncoder.encode(string, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlString;
    }

}
