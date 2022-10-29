package com.example.demo.server;

import com.example.demo.model.User;

import java.util.ArrayList;
import java.util.List;

public class SessionManager {

    List<Session> sessionList = new ArrayList<>();

    public List<User> getOnlineList() {
        List<User> a = new ArrayList<>();
        for (Session session : sessionList) {
            a.add(session.getUser());
        }
        return a;
    }


    public void removeSession(Session session) {
        sessionList.remove(session);
    }

    public void addSession(Session session){
        sessionList.add(session);
    }

    public Session getSessionById(int userId) {
        for (Session session : sessionList) {
            if (session.getUser().getId() == userId) {
                return session;
            }
        }
        return null;
    }
}
