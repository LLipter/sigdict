package com.llipter.sigdict.entity;

import com.llipter.sigdict.security.DigitalSignature;
import com.llipter.sigdict.security.HashPassword;
import com.llipter.sigdict.security.SignatureType;
import com.llipter.sigdict.utility.Utility;

import javax.persistence.*;
import java.security.KeyPair;

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

    @Column(length = 2048)
    private String DsaPublicKey;

    @Column(length = 1024)
    private String DsaPrivateKey;

    @Column(length = 512)
    private String RsaPublicKey;

    @Column(length = 2048)
    private String RsaPrivateKey;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Session session;

    public User() {

    }

    public User(String username, String password, String email) {
        byte[] salt = HashPassword.getSalt();
        this.setUsername(username);
        this.setHashedPassword(HashPassword.getHashedPassword(password, salt));
        this.setEmail(email);
        this.setSalt(Utility.binary2base64(salt));

        KeyPair keyPair = DigitalSignature.generateKeyPair(SignatureType.DSA);
        this.setDsaPrivateKey(Utility.binary2base64(keyPair.getPrivate().getEncoded()));
        this.setDsaPublicKey(Utility.binary2base64(keyPair.getPublic().getEncoded()));
        keyPair = DigitalSignature.generateKeyPair(SignatureType.RSA);
        this.setRsaPrivateKey(Utility.binary2base64(keyPair.getPrivate().getEncoded()));
        this.setRsaPublicKey(Utility.binary2base64(keyPair.getPublic().getEncoded()));
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

    public String getDsaPublicKey() {
        return DsaPublicKey;
    }

    public void setDsaPublicKey(String dsaPublicKey) {
        DsaPublicKey = dsaPublicKey;
    }

    public String getDsaPrivateKey() {
        return DsaPrivateKey;
    }

    public void setDsaPrivateKey(String dsaPrivateKey) {
        DsaPrivateKey = dsaPrivateKey;
    }

    public String getRsaPublicKey() {
        return RsaPublicKey;
    }

    public void setRsaPublicKey(String rsaPublicKey) {
        RsaPublicKey = rsaPublicKey;
    }

    public String getRsaPrivateKey() {
        return RsaPrivateKey;
    }

    public void setRsaPrivateKey(String rsaPrivateKey) {
        RsaPrivateKey = rsaPrivateKey;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public boolean validatePassword(String password) {
        String hashedPassword = HashPassword.getHashedPassword(password, Utility.base642binary(this.getSalt()));
        if (hashedPassword.equals(this.getHashedPassword()))
            return true;
        return false;
    }
}
