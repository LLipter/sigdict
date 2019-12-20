package com.llipter.sigdict.utility;

import org.springframework.util.StringUtils;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;

public class ValidateInput {
    private ValidateInput() {
    }

    public static boolean isAsciiDigit(char c) {
        return '0' <= c && c <= '9';
    }

    public static boolean isAsciiUpperCaseLetter(char c) {
        return 'A' <= c && c <= 'Z';
    }

    public static boolean isAsciiLowerCaseLetter(char c) {
        return 'a' <= c && c <= 'z';
    }

    public static boolean isAsciiLetter(char c) {
        return isAsciiLowerCaseLetter(c) || isAsciiUpperCaseLetter(c);
    }

    public static boolean isAsciiLetterOrDigit(char c) {
        return isAsciiLetter(c) || isAsciiDigit(c);
    }

    public static boolean isAsciiPrintable(char c) {
        return c > 31 && c < 127;
    }

    public static boolean isAsciiSpecial(char c) {
        return isAsciiPrintable(c) && !isAsciiLetterOrDigit(c);
    }

    // A valid username must contain no less than 6 characters and no more than 18 characters
    // Each character must be must of the followings
    //  - a ASCII digit
    //  - a ASCII letter
    //  - '_'
    public static boolean isValidUsername(String username) {
        if (username.length() < 6)
            return false;
        if (username.length() > 18)
            return false;
        for (char c : username.toCharArray()) {
            if (!isAsciiLetterOrDigit(c) && c != '_') {
                return false;
            }

        }
        return true;
    }

    // A valid password must contain no less than 6 characters and no more than 18 characters
    // Each character must be a printable ASCII character
    // Must contain uppercase letter, lowercase letter, digit, special character at the same time.
    public static boolean isValidPassword(String password) {
        if (password.length() < 6)
            return false;
        if (password.length() > 18)
            return false;
        boolean containUpperCaseLetter = false;
        boolean containLowerCaseLetter = false;
        boolean containDigit = false;
        boolean containSpecial = false;
        for (char c : password.toCharArray()) {
            if (!isAsciiPrintable(c))
                return false;
            if (isAsciiUpperCaseLetter(c))
                containUpperCaseLetter = true;
            if (isAsciiLowerCaseLetter(c))
                containLowerCaseLetter = true;
            if (isAsciiDigit(c))
                containDigit = true;
            if (isAsciiSpecial(c))
                containSpecial = true;
        }
        return containUpperCaseLetter && containLowerCaseLetter && containDigit && containSpecial;
    }

    public static boolean isValidEmail(String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            return true;
        } catch (AddressException ex) {
            return false;
        }
    }

    public static boolean isValidFilename(String filename) {
        if (!filename.contains("."))
            return false;
        return !filename.contains(File.separator) && !filename.contains(File.pathSeparator);
    }

    private static String[] validExtensions =
            {
                    "docx", "doc", "pptx", "ppt", "xlsx", "xls",
                    "jpeg", "jpg", "png", "gif", "tiff", "tif", "raw", "bmp", "svg",
                    "txt", "tex", "md", "pdf",
                    "mp3", "wav",
                    "zip", "z", "rar", "7z", "gz",
                    "csv", "xml", "tar"
            };

    public static boolean hasValidExtention(String filename) {
        String extension = StringUtils.getFilenameExtension(filename).toLowerCase();
        for (String validExtension : validExtensions) {
            if (extension.equals(validExtension))
                return true;
        }
        return false;
    }
}
