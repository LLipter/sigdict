package com.llipter.sigdict.test;

import com.llipter.sigdict.utility.ValidateInput;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UtilityTest {
    public static void main(String[] args) {
        char c = 'b';
        System.out.println(c > 'a');
        System.out.println('_' < 'z');
        System.out.println('_' > 'a');
        System.out.println('a');

        String email = "llipter@foxmail.com";
        System.out.println(ValidateInput.isValidEmail(email));
        email = "llipters.@^%&%^*%&^";
        System.out.println(ValidateInput.isValidEmail(email));

        String username = "liaoran12";
        System.out.println(ValidateInput.isValidUsername(username));

        String password = "123411213Rs";
        System.out.println(ValidateInput.isValidPassword(password));

        System.out.println(ValidateInput.isAsciiLetterOrDigit('o'));

        System.out.println(System.getProperty("os.name"));

        String filename = ". ";
        System.out.println(StringUtils.getFilenameExtension(filename).toLowerCase());

        Path rootLocation = Paths.get("upload-dir");
        Path ano = rootLocation.resolve("X");

        System.out.println(ano);

        String s = "///sdw:qr2/4ref";
        s = s.replaceAll(File.separator, "-");
        s = s.replace(File.pathSeparator, "-");
        System.out.println(s);
    }
}
