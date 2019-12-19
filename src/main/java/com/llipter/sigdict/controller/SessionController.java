package com.llipter.sigdict.controller;

import com.llipter.sigdict.entity.Session;
import com.llipter.sigdict.entity.User;
import com.llipter.sigdict.repository.SessionRepository;
import com.llipter.sigdict.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

// this class focuses on session control
public abstract class SessionController {

    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    protected UserRepository userRepository;

    @Autowired
    protected SessionRepository sessionRepository;

    public boolean validateSession(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String sessionId = Session.getSessionIdFromCookies(cookies);
        if (validateSessionId(sessionId)) {
            return true;
        }
        return false;
    }

    public boolean validateSessionId(String sessionId) {
        if (sessionId == null)
            return false;

        Session session = sessionRepository.findBySessionId(sessionId);
        if (session == null)
            return false;

        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (now.after(session.getValidThrough())) {
            // delete expired session
            deleteSession(session);
            return false;
        }

        // session is valid
        // refresh session
        refreshSession(session);

        return true;
    }

    public void refreshSession(Session session) {
        session.setValidThrough(Session.getRefreshedValidThrough());
        sessionRepository.save(session);
    }

    public void deleteSession(Session session) {
        User user = session.getUser();
        session.setUser(null);
        user.setSession(null);
        userRepository.save(user);
        sessionRepository.delete(session);
    }

    public void deleteSession(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String sessionId = Session.getSessionIdFromCookies(cookies);
        if (sessionId == null)
            return;

        Session session = sessionRepository.findBySessionId(sessionId);
        if (session == null)
            return;

        deleteSession(session);
    }
}
