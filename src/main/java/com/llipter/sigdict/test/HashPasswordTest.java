package com.llipter.sigdict.test;

import com.llipter.sigdict.security.HashPassword;
import com.llipter.sigdict.utility.Utility;

public class HashPasswordTest {
    public static void main(String[] args) {


        System.out.println("Username: llipter");
        System.out.println("Password: cestlavie");
        byte[] salt = HashPassword.getSalt();
        String hashedPassword = Utility.binary2base64(HashPassword.getHashedPassword("cestlavie", salt));
        System.out.println("HashedPassword: " + hashedPassword);
        System.out.println("Salt: " + Utility.binary2base64(salt));

        System.out.println("Username: epfl");
        System.out.println("Password: eth");
        salt = HashPassword.getSalt();
        hashedPassword = Utility.binary2base64(HashPassword.getHashedPassword("eth", salt));
        System.out.println("HashedPassword: " + hashedPassword);
        System.out.println("Salt: " + Utility.binary2base64(salt));
    }
}
