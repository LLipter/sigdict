package com.llipter.sigdict.entity;

import com.llipter.sigdict.security.DigitalSignature;
import com.llipter.sigdict.security.HashPassword;
import com.llipter.sigdict.security.Utility;

import javax.persistence.*;
import java.security.KeyPair;

@Entity // This tells Hibernate to make a table out of this class
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer uid;

    @Column(unique = true)
    private String username;

    private String hashed_password;

    private String email;

    private String salt;

    @Column(length=2048)
    private String public_key;

    @Column(length=1024)
    private String private_key;

    public User() {

    }

    public User(String username, String password, String email) {
        byte[] salt = HashPassword.getSalt();
        this.setUsername(username);
        this.setHashed_password(HashPassword.getHashedPassword(password, salt));
        KeyPair keyPair = DigitalSignature.generateKeyPair();
        this.setEmail(email);
        this.setSalt(Utility.binary2base64(salt));
        this.setPrivate_key(Utility.binary2base64(keyPair.getPrivate().getEncoded()));
        this.setPublic_key(Utility.binary2base64(keyPair.getPublic().getEncoded()));
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

    public String getHashed_password() {
        return hashed_password;
    }

    public void setHashed_password(String hashed_password) {
        this.hashed_password = hashed_password;
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

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public boolean validatePassword(String password) {
        String hashedPassword = HashPassword.getHashedPassword(password, Utility.base642binary(this.getSalt()));
        if (hashedPassword.equals(this.getHashed_password()))
            return true;
        return false;
    }
}
