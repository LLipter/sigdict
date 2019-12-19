package com.llipter.sigdict.entity;

import com.llipter.sigdict.Utility;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity // This tells Hibernate to make a table out of this class
public class Session {
    public static final int SESSION_ID_SIZE = 16;

    @Id
    private Integer uid;

    private String session_id;

    @Basic
    private java.sql.Timestamp valid_through;

    @OneToOne
    @MapsId // @MapsId tells Hibernate to use the id column of this entity as both primary key and foreign key.
    private User user;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public Timestamp getValid_through() {
        return valid_through;
    }

    public void setValid_through(Timestamp valid_through) {
        this.valid_through = valid_through;
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
