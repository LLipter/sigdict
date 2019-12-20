package com.llipter.sigdict.entity;

import com.llipter.sigdict.security.DigitalSignature;
import com.llipter.sigdict.security.HashPassword;
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
    private String publicKey;

    @Column(length = 1024)
    private String privateKey;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Session session;

    public User() {

    }

    public User(String username, String password, String email) {
        byte[] salt = HashPassword.getSalt();
        this.setUsername(username);
        this.setHashedPassword(HashPassword.getHashedPassword(password, salt));
        KeyPair keyPair = DigitalSignature.generateKeyPair();
        this.setEmail(email);
        this.setSalt(Utility.binary2base64(salt));
        this.setPrivateKey(Utility.binary2base64(keyPair.getPrivate().getEncoded()));
        this.setPublicKey(Utility.binary2base64(keyPair.getPublic().getEncoded()));
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

    public void setHashedPassword(String hashed_password) {
        this.hashedPassword = hashed_password;
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

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String public_key) {
        this.publicKey = public_key;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String private_key) {
        this.privateKey = private_key;
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
