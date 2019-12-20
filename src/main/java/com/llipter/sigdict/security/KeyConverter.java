package com.llipter.sigdict.security;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyConverter {
    static String SYMMETRIC_KEY_GENERATION_ALGORITHM = "AES";

    private static KeyFactory getKeyFactory(SignatureType signatureType) {
        KeyFactory keyFactory = null;
        try {
            if (signatureType == SignatureType.DSA)
                keyFactory = KeyFactory.getInstance("DSA");
            else
                keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyFactory;
    }

    private static Key bytes2AsymmetricKey(byte[] key, SignatureType signatureType, boolean isPublic) {
        KeyFactory keyFactory = getKeyFactory(signatureType);
        Key asymmetricKey = null;
        try {
            if (isPublic) {
                asymmetricKey = keyFactory.generatePublic(new X509EncodedKeySpec(key));
            } else {
                asymmetricKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(key));
            }
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return asymmetricKey;
    }

    private static PrivateKey bytes2PrivateKey(byte[] key, SignatureType signatureType) {
        return (PrivateKey) bytes2AsymmetricKey(key, signatureType, false);
    }

    public static PublicKey bytes2PublicKey(byte[] key, SignatureType signatureType) {
        return (PublicKey) bytes2AsymmetricKey(key, signatureType, true);
    }

    public static PrivateKey bytes2DsaPrivateKey(byte[] key) {
        return bytes2PrivateKey(key, SignatureType.DSA);
    }

    public static PublicKey bytes2DsaPublicKey(byte[] key) {
        return bytes2PublicKey(key, SignatureType.DSA);
    }

    public static PrivateKey bytes2RsaPrivateKey(byte[] key) {
        return bytes2PrivateKey(key, SignatureType.RSA);
    }

    public static PublicKey bytes2RsaPublicKey(byte[] key) {
        return bytes2PublicKey(key, SignatureType.RSA);
    }

    public static SecretKey bytes2SecretKey(byte[] key) {
        return new SecretKeySpec(key, 0, key.length, SYMMETRIC_KEY_GENERATION_ALGORITHM);
    }
}
