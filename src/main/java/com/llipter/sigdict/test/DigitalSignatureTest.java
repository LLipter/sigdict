package com.llipter.sigdict.test;

import com.llipter.sigdict.security.DigitalSignature;
import java.security.KeyPair;
import com.llipter.sigdict.security.Utility;

public class DigitalSignatureTest {
    public static void main(String[] args) {
        KeyPair keyPair = DigitalSignature.generateKeyPair();
        System.out.println(keyPair.getPrivate().getEncoded().length);
        System.out.println(keyPair.getPublic().getEncoded().length);
        System.out.println("Private Key: " + Utility.binary2base64(keyPair.getPrivate().getEncoded()));
        System.out.println("Public Key: " + Utility.binary2base64(keyPair.getPublic().getEncoded()));
        keyPair = DigitalSignature.generateKeyPair();
        System.out.println("Private Key: " + Utility.binary2base64(keyPair.getPrivate().getEncoded()));
        System.out.println("Public Key: " + Utility.binary2base64(keyPair.getPublic().getEncoded()));


    }
}
