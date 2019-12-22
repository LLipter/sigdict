package com.llipter.sigdict.entity;

import com.llipter.sigdict.security.*;
import com.llipter.sigdict.utility.Storage;

import javax.crypto.SecretKey;
import javax.persistence.*;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity // This tells Hibernate to make a table out of this class
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer uid;

    @Column(unique = true)
    private String username;

    private byte[] hashedPassword;

    private String email;

    private boolean verified;

    @Column(length = 16)
    private byte[] salt;

    // 838
    @Column(length = 1024)
    private byte[] DsaPublicKey;

    // 608-609
    @Column(length = 1024)
    private byte[] DsaPrivateKey;

    // 294
    @Column(length = 512)
    private byte[] RsaPublicKey;

    // 1217 - 1219
    @Column(length = 2048)
    private byte[] RsaPrivateKey;

    // unencrypted = 32
    // encrypted = 48
    // iv = 16
    // 48 + 16 = 64
    @Column(length = 64)
    private byte[] userMasterKey;

    // unencrypted = 32
    // encrypted = 48
    // iv = 16
    // 48 + 16 = 64
    @Column(length = 64)
    private byte[] userEncryptionKey;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Session session;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private VerificationToken verificationToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ResetPasswordToken resetPasswordToken;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UploadedFile> uploadedFiles;

    public User() {
        this.setUploadedFiles(new ArrayList<>());
    }

    public User(String username, String password, String email) {
        byte[] salt = HashPassword.getSalt();
        this.setUsername(username);
        this.setHashedPassword(HashPassword.getHashedPassword(password, salt));
        this.setEmail(email);
        this.setVerified(false);
        this.setSalt(salt);
        this.setUploadedFiles(new ArrayList<>());

        SecretKey userMasterKey = SymmetricEncryption.generateKey();
        SecretKey userEncryptionKey = SymmetricEncryption.generateKey();
        KeyPair dsaKey = DigitalSignature.generateKeyPair(SignatureType.DSA);
        KeyPair rsaKey = DigitalSignature.generateKeyPair(SignatureType.RSA);
        storeAndEncryptedAllKeys(
                userMasterKey,
                userEncryptionKey,
                dsaKey,
                rsaKey);
    }

    private void storeAndEncryptedAllKeys(
            SecretKey userMasterKey,
            SecretKey userEncryptionKey,
            KeyPair dsaKey,
            KeyPair rsaKey) {
        this.setUserMasterKey(SymmetricEncryption.encryptWithApplicationMasterKey(userMasterKey.getEncoded()));
        this.setUserEncryptionKey(SymmetricEncryption.encrypt(userMasterKey, userEncryptionKey.getEncoded()));
        this.setDsaPrivateKey(SymmetricEncryption.encrypt(userMasterKey, dsaKey.getPrivate().getEncoded()));
        this.setDsaPublicKey(dsaKey.getPublic().getEncoded());
        this.setRsaPrivateKey(SymmetricEncryption.encrypt(userMasterKey, rsaKey.getPrivate().getEncoded()));
        this.setRsaPublicKey(rsaKey.getPublic().getEncoded());
    }

    public void changePassword(String newPassword) {
        byte[] hashedPassword = HashPassword.getHashedPassword(newPassword, this.getSalt());
        this.setHashedPassword(hashedPassword);
    }

    public void changeEmail(String email) {
        this.setEmail(email);
        this.setVerified(false);
    }

    public SecretKey getUnencryptedUserMasterKey() {
        byte[] masterKey = SymmetricEncryption.decryptWithApplicationMasterKey(getUserMasterKey());
        return KeyConverter.bytes2SecretKey(masterKey);
    }

    public SecretKey getUnencryptedUserEncryptionKey() {
        SecretKey masterKey = getUnencryptedUserMasterKey();
        byte[] encryptionKey = SymmetricEncryption.decrypt(masterKey, getUserEncryptionKey());
        return KeyConverter.bytes2SecretKey(encryptionKey);
    }

    public PrivateKey getUnencryptedDsaPrivateKey() {
        SecretKey masterKey = getUnencryptedUserMasterKey();
        byte[] dsaPrivateKey = SymmetricEncryption.decrypt(masterKey, getDsaPrivateKey());
        return KeyConverter.bytes2DsaPrivateKey(dsaPrivateKey);
    }

    public PrivateKey getUnencryptedRsaPrivateKey() {
        SecretKey masterKey = getUnencryptedUserMasterKey();
        byte[] rsaPrivateKey = SymmetricEncryption.decrypt(masterKey, getRsaPrivateKey());
        return KeyConverter.bytes2RsaPrivateKey(rsaPrivateKey);
    }

    public UploadedFile getUploadedFileByIdentifier(String identifier) {
        for (UploadedFile uploadedFile : getUploadedFiles()) {
            if (uploadedFile.getIdentifier().equals(identifier))
                return uploadedFile;
        }
        return null;
    }

    public void removeUploadedFileByIdentifier(String identifier) {
        List<UploadedFile> uploadedFiles = this.getUploadedFiles();
        for (int i = 0; i < uploadedFiles.size(); i++) {
            if (uploadedFiles.get(i).getIdentifier().equals(identifier)) {
                uploadedFiles.remove(i);
                return;
            }
        }
    }

    public boolean validatePassword(String password) {
        byte[] hashedPassword = HashPassword.getHashedPassword(password, this.getSalt());
        return Arrays.equals(hashedPassword, this.getHashedPassword());
    }

    public void changeKey() {
        // generate new keys
        SecretKey newUserMasterKey = SymmetricEncryption.generateKey();
        SecretKey newUserEncryptionKey = SymmetricEncryption.generateKey();
        KeyPair newDsaKey = DigitalSignature.generateKeyPair(SignatureType.DSA);
        KeyPair newRsaKey = DigitalSignature.generateKeyPair(SignatureType.RSA);

        // upload all files
        for (UploadedFile uploadedFile : this.getUploadedFiles()) {
            updateFileWithNewKey(
                    uploadedFile,
                    newDsaKey.getPrivate(),
                    newRsaKey.getPrivate(),
                    newUserEncryptionKey,
                    this.getUnencryptedUserEncryptionKey());
        }

        // store new keys
        storeAndEncryptedAllKeys(
                newUserMasterKey,
                newUserEncryptionKey,
                newDsaKey,
                newRsaKey);
    }

    private void updateFileWithNewKey(
            UploadedFile file,
            PrivateKey newDsaPrivateKey,
            PrivateKey newRsaPrivateKey,
            SecretKey newEncryptionKey,
            SecretKey oldEncryptionKey) {

        // load file from disk
        byte[] data = Storage.loadFileAsBytes(file.getIdentifier());

        // decrypt data if necessary
        if (file.isEncrypted()) {
            data = SymmetricEncryption.decrypt(oldEncryptionKey, data);
        }

        // generate new signature
        byte[] newSignature;
        if (file.getSignatureType() == SignatureType.DSA) {
            newSignature = DigitalSignature.sign(
                    SignatureType.DSA,
                    newDsaPrivateKey,
                    data);
        } else {
            newSignature = DigitalSignature.sign(
                    SignatureType.RSA,
                    newRsaPrivateKey,
                    data);
        }
        file.setSignature(newSignature);

        // encrypt data if necessary
        if (file.isEncrypted()) {
            data = SymmetricEncryption.encrypt(newEncryptionKey, data);
        }

        // store data to disk
        Storage.store(data, file.getIdentifier());
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(byte[] hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public byte[] getDsaPublicKey() {
        return DsaPublicKey;
    }

    public void setDsaPublicKey(byte[] dsaPublicKey) {
        DsaPublicKey = dsaPublicKey;
    }

    public byte[] getDsaPrivateKey() {
        return DsaPrivateKey;
    }

    public void setDsaPrivateKey(byte[] dsaPrivateKey) {
        DsaPrivateKey = dsaPrivateKey;
    }

    public byte[] getRsaPublicKey() {
        return RsaPublicKey;
    }

    public void setRsaPublicKey(byte[] rsaPublicKey) {
        RsaPublicKey = rsaPublicKey;
    }

    public byte[] getRsaPrivateKey() {
        return RsaPrivateKey;
    }

    public void setRsaPrivateKey(byte[] rsaPrivateKey) {
        RsaPrivateKey = rsaPrivateKey;
    }

    public byte[] getUserMasterKey() {
        return userMasterKey;
    }

    public void setUserMasterKey(byte[] userMasterKey) {
        this.userMasterKey = userMasterKey;
    }

    public byte[] getUserEncryptionKey() {
        return userEncryptionKey;
    }

    public void setUserEncryptionKey(byte[] userEncryptionKey) {
        this.userEncryptionKey = userEncryptionKey;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public VerificationToken getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(VerificationToken verificationToken) {
        this.verificationToken = verificationToken;
    }

    public ResetPasswordToken getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(ResetPasswordToken resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public List<UploadedFile> getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(List<UploadedFile> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }
}
