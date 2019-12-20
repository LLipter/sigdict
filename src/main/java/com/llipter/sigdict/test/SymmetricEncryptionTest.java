package com.llipter.sigdict.test;

import com.llipter.sigdict.security.SymmetricEncryption;
import com.llipter.sigdict.utility.Utility;

import javax.crypto.SecretKey;

public class SymmetricEncryptionTest {
    public static void main(String[] args) {
        String data = "1234567890";
        SecretKey key = SymmetricEncryption.generateKey();
        System.out.println(key.getEncoded().length);
        System.out.println(Utility.binary2base64(data.getBytes()));
        byte[] encryptedData = SymmetricEncryption.encrypt(key, data.getBytes());
        System.out.println(Utility.binary2base64(encryptedData));
        byte[] decryptedData = SymmetricEncryption.decrypt(key, encryptedData);
        System.out.println(Utility.binary2base64(decryptedData));

//        System.out.println(key.);
    }
}
