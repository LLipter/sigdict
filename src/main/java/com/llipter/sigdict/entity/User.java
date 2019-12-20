package com.llipter.sigdict.entity;

import com.llipter.sigdict.security.DigitalSignature;
import com.llipter.sigdict.security.HashPassword;
import com.llipter.sigdict.security.SignatureType;
import com.llipter.sigdict.security.SymmetricEncryption;
import com.llipter.sigdict.utility.Utility;

import javax.persistence.*;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

@Entity // This tells Hibernate to make a table out of this class
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer uid;

    @Column(unique = true)
    private String username;

    private String hashedPassword;

    private String email;

    private String salt;

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
        this.setSalt(Utility.binary2base64(salt));
        this.setUploadedFiles(new ArrayList<>());

        // user master key
        byte[] masterKey = SymmetricEncryption.generateKey().getEncoded();
        this.setUserMasterKey(SymmetricEncryption.encryptWithApplicationMasterKey(masterKey));

        // user encryption key
        byte[] encryptionKey = SymmetricEncryption.generateKey().getEncoded();
        this.setUserEncryptionKey(SymmetricEncryption.encrypt(masterKey, encryptionKey));

        // dsa key pairs
        KeyPair keyPair = DigitalSignature.generateKeyPair(SignatureType.DSA);
        this.setDsaPrivateKey(SymmetricEncryption.encrypt(masterKey, keyPair.getPrivate().getEncoded()));
        this.setDsaPublicKey(keyPair.getPublic().getEncoded());

        // rsa key pairs
        keyPair = DigitalSignature.generateKeyPair(SignatureType.RSA);
        this.setRsaPrivateKey(SymmetricEncryption.encrypt(masterKey, keyPair.getPrivate().getEncoded()));
        this.setRsaPublicKey(keyPair.getPublic().getEncoded());
    }


    public boolean validatePassword(String password) {
        String hashedPassword = HashPassword.getHashedPassword(password, Utility.base642binary(this.getSalt()));
        if (hashedPassword.equals(this.getHashedPassword()))
            return true;
        return false;
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

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
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

    public List<UploadedFile> getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(List<UploadedFile> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }
}
