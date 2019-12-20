package com.llipter.sigdict.test;

import com.llipter.sigdict.utility.ValidateInput;

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
    }
}
