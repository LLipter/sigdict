package com.llipter.sigdict.entity;

import com.llipter.sigdict.utility.Utility;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity // This tells Hibernate to make a table out of this class
public class ResetPasswordToken {
    public static final int TOKEN_SIZE = 16;

    // Session will be valid for 30 minutes
    public static final int TOKEN_VALID_TIME_SPAN_SECOND = 30 * 60;
    public static final int TOKEN_VALID_TIME_SPAN_MILLISECOND = TOKEN_VALID_TIME_SPAN_SECOND * 1000;

    @Id
    private Integer uid;

    private String token;

    @Basic
    private Timestamp validThrough;

    @OneToOne
    @MapsId // @MapsId tells Hibernate to use the id column of this entity as both primary key and foreign key.
    private User user;

    public ResetPasswordToken() {
        this.setToken(generateToken());
        this.setValidThrough(getRefreshedValidThrough());
    }

    public ResetPasswordToken(User user) {
        this();
        this.setUser(user);
    }

    public static String generateToken() {
        return Utility.binary2base64(Utility.getRandomBytes(TOKEN_SIZE));
    }

    public static Timestamp getRefreshedValidThrough() {
        return new Timestamp(System.currentTimeMillis() + TOKEN_VALID_TIME_SPAN_MILLISECOND);
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getValidThrough() {
        return validThrough;
    }

    public void setValidThrough(Timestamp validThrough) {
        this.validThrough = validThrough;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
