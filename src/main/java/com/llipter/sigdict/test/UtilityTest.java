package com.llipter.sigdict.test;

import com.llipter.sigdict.Utility;

public class UtilityTest {
    public static void main(String[] args) {
        char c = 'b';
        System.out.println(c > 'a');
        System.out.println('_' < 'z');
        System.out.println('_' > 'a');
        System.out.println('a');

        String email = "llipter@foxmail.com";
        System.out.println(Utility.isValidEmail(email));
        email = "llipters.@^%&%^*%&^";
        System.out.println(Utility.isValidEmail(email));

        String username = "liaoran12";
        System.out.println(Utility.isValidUsername(username));

        String password = "123411213Rs";
        System.out.println(Utility.isValidPassword(password));

        System.out.println(Utility.isAsciiLetterOrDigit('o'));
    }
}
