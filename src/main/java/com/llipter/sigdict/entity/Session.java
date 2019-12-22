package com.llipter.sigdict.entity;

import com.llipter.sigdict.utility.Utility;

import javax.persistence.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

@Entity // This tells Hibernate to make a table out of this class
public class Session {
    public static final int SESSION_ID_SIZE = 16;

    // Session will be valid for 30 minutes
    public static final int SESSION_VALID_TIME_SPAN_SECOND = 30 * 60;
    public static final int SESSION_VALID_TIME_SPAN_MILLISECOND = SESSION_VALID_TIME_SPAN_SECOND * 1000;

    public static final String SESSION_COOKIE_NAME = "SESSION_ID";


    @Id
    private Integer uid;

    private String sessionId;

    @Basic
    private Timestamp validThrough;

    @OneToOne
    @MapsId // @MapsId tells Hibernate to use the id column of this entity as both primary key and foreign key.
    private User user;

    public Session() {
        this.setSessionId(generateSessionId());
        this.setValidThrough(getRefreshedValidThrough());
    }

    public Session(User user) {
        this();
        this.setUser(user);
    }

    public static String generateSessionId() {
        return Utility.binary2base64(Utility.getRandomBytes(SESSION_ID_SIZE));
    }

    public static String getSessionIdFromCookies(Cookie[] cookies) {
        if (cookies == null)
            return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(SESSION_COOKIE_NAME)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static Timestamp getRefreshedValidThrough() {
        return new Timestamp(System.currentTimeMillis() + SESSION_VALID_TIME_SPAN_MILLISECOND);
    }

    public static void setSessionCookie(HttpServletResponse response, String sessionId) {
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        cookie.setMaxAge(SESSION_VALID_TIME_SPAN_SECOND);
        response.addCookie(cookie);
    }

    public void refresh() {
        this.setValidThrough(getRefreshedValidThrough());
        this.setSessionId(generateSessionId());
    }

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
}
