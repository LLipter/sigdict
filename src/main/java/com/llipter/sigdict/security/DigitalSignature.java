package com.llipter.sigdict.security;

import com.llipter.sigdict.utility.Utility;

import java.security.*;

public class DigitalSignature {
    public static KeyPair generateKeyPair(SignatureType signatureType) {
        KeyPairGenerator keyPairGenerator = null;
        try {
            if (signatureType == SignatureType.DSA) {
                keyPairGenerator = KeyPairGenerator.getInstance("DSA");
            } else {
                keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyPairGenerator.generateKeyPair();
    }

    public static String sign(SignatureType signatureType, PrivateKey privateKey, byte[] data) {
        Signature signature = null;
        try {
            if (signatureType == SignatureType.DSA) {
                signature = Signature.getInstance("SHA256WithDSA");
            } else {
                signature = Signature.getInstance("SHA256withRSA");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        SecureRandom secureRandom = new SecureRandom();
        byte[] digitalSignature = null;
        try {
            signature.initSign(privateKey, secureRandom);
            signature.update(data);
            digitalSignature = signature.sign();
        } catch (InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }

        return Utility.binary2base64(digitalSignature);
    }
}
