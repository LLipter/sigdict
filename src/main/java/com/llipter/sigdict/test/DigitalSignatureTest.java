package com.llipter.sigdict.test;

import com.llipter.sigdict.security.DigitalSignature;
import com.llipter.sigdict.security.KeyConverter;
import com.llipter.sigdict.security.SignatureType;
import com.llipter.sigdict.utility.Utility;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;

public class DigitalSignatureTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
        KeyPair keyPair = DigitalSignature.generateKeyPair(SignatureType.DSA);
        System.out.println(keyPair.getPrivate().getEncoded().length);
        System.out.println(keyPair.getPublic().getEncoded().length);
        System.out.println("Private Key: " + Utility.binary2base64(keyPair.getPrivate().getEncoded()));
        System.out.println("Public Key: " + Utility.binary2base64(keyPair.getPublic().getEncoded()));
        keyPair = DigitalSignature.generateKeyPair(SignatureType.DSA);
        System.out.println("Private Key: " + Utility.binary2base64(keyPair.getPrivate().getEncoded()));
        System.out.println("Public Key: " + Utility.binary2base64(keyPair.getPublic().getEncoded()));

        System.out.println("Private Key: " + Utility.binary2base64(KeyConverter.bytes2DsaPrivateKey(keyPair.getPrivate().getEncoded()).getEncoded()));
        System.out.println("Public Key: " + Utility.binary2base64(KeyConverter.bytes2DsaPublicKey(keyPair.getPublic().getEncoded()).getEncoded()));


        String data = "123456723421432489";
        byte[] signature = DigitalSignature.sign(SignatureType.DSA, keyPair.getPrivate(), data.getBytes());
        System.out.println("Signature: " + Utility.binary2base64(signature));
        System.out.println("Signature Size: " + signature.length);

        keyPair = DigitalSignature.generateKeyPair(SignatureType.RSA);
        System.out.println(keyPair.getPrivate().getEncoded().length);
        System.out.println(keyPair.getPublic().getEncoded().length);
        System.out.println("Private Key: " + Utility.binary2base64(keyPair.getPrivate().getEncoded()));
        System.out.println("Public Key: " + Utility.binary2base64(keyPair.getPublic().getEncoded()));

        signature = DigitalSignature.sign(SignatureType.RSA, keyPair.getPrivate(), data.getBytes());
        System.out.println("Signature: " + Utility.binary2base64(signature));
        System.out.println("Signature Size: " + signature.length);

        System.out.println("Private Key: " + Utility.binary2base64(KeyConverter.bytes2RsaPrivateKey(keyPair.getPrivate().getEncoded()).getEncoded()));
        System.out.println("Public Key: " + Utility.binary2base64(KeyConverter.bytes2RsaPublicKey(keyPair.getPublic().getEncoded()).getEncoded()));


    }
}
