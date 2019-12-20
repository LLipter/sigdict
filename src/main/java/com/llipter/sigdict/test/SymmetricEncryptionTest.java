package com.llipter.sigdict.test;

import com.llipter.sigdict.security.SymmetricEncryption;
import com.llipter.sigdict.utility.Utility;

import java.security.Key;

public class SymmetricEncryptionTest {
    public static void main(String[] args) {
        String data = "1234567890";
        Key key = SymmetricEncryption.generateKey();
        System.out.println(Utility.binary2base64(data.getBytes()));
        byte[] encryptedData = SymmetricEncryption.encrypt(key, data.getBytes());
        System.out.println(Utility.binary2base64(encryptedData));
        byte[] decryptedData = SymmetricEncryption.decrypt(key, encryptedData);
        System.out.println(Utility.binary2base64(decryptedData));
    }
}
