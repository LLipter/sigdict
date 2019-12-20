package com.llipter.sigdict.security;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SymmetricEncryption {

    private static SecretKey masterKey;

    private static int AES_KEY_SIZE = 256;

    private static int IV_SIZE = 16;

    public static SecretKey generateKey() {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        SecureRandom secureRandom = new SecureRandom();
        keyGenerator.init(AES_KEY_SIZE, secureRandom);
        return keyGenerator.generateKey();
    }

    private static Cipher getCipher(SecretKey key, int mode, IvParameterSpec ivParameterSpec) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(mode, key, ivParameterSpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return cipher;
    }

    public static byte[] encrypt(SecretKey key, byte[] data) {
        // generate iv randomly
        byte[] iv = new byte[IV_SIZE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = getCipher(key, Cipher.ENCRYPT_MODE, ivParameterSpec);
        byte[] encryptedData = null;
        try {
            encryptedData = cipher.doFinal(data);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        // combine iv and encrypted data
        byte[] ivAndEncryptedData = new byte[IV_SIZE + encryptedData.length];
        System.arraycopy(iv, 0, ivAndEncryptedData, 0, IV_SIZE);
        System.arraycopy(encryptedData, 0, ivAndEncryptedData, IV_SIZE, encryptedData.length);
        return ivAndEncryptedData;
    }

    public static byte[] decrypt(SecretKey key, byte[] data) {
        // extract iv
        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(data, 0, iv, 0, IV_SIZE);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // extract encrypted data
        int encryptedDataSize = data.length - IV_SIZE;
        byte[] encryptedData = new byte[encryptedDataSize];
        System.arraycopy(data, IV_SIZE, encryptedData, 0, encryptedDataSize);

        Cipher cipher = getCipher(key, Cipher.DECRYPT_MODE, ivParameterSpec);
        byte[] decryptedData = null;
        try {
            decryptedData = cipher.doFinal(encryptedData);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return decryptedData;
    }

    public static SecretKey getMasterKey() {
        return masterKey;
    }

    public static void setMasterKey(SecretKey masterKey) {
        SymmetricEncryption.masterKey = masterKey;
    }

    public static void generateMasterKey() {
        setMasterKey(generateKey());
    }
}
