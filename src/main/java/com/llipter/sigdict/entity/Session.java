package com.llipter.sigdict.entity;

import com.llipter.sigdict.Utility;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity // This tells Hibernate to make a table out of this class
public class Session {
    public static final int SESSION_ID_SIZE = 16;

    @Id
    private Integer uid;

    private String sessionId;

    @Basic
    private java.sql.Timestamp validThrough;

    @OneToOne
    @MapsId // @MapsId tells Hibernate to use the id column of this entity as both primary key and foreign key.
    private User user;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String session_id) {
        this.sessionId = session_id;
    }

    public Timestamp getValidThrough() {
        return validThrough;
    }

    public void setValidThrough(Timestamp valid_through) {
        this.validThrough = valid_through;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static String generateSessionId() {
        return Utility.binary2base64(Utility.getRandomBytes(SESSION_ID_SIZE));
    }

}
