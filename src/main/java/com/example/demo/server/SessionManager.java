package com.example.demo.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {

    List<Session> sessionList = new ArrayList<>();

    public List<String> getOnlineList(){
        List<String> a = new ArrayList<>();
        for (Session session : sessionList) {
            a.add(session.getNickName());
        }
        return a;
    }


    public void removeSession(Session session){
        sessionList.remove(session);
    }

    public void addSession(Session session){
        sessionList.add(session);
    }

    public Session getSessionByNickName(String nickName){
        if(nickName==null && nickName.length()==0){
            return null;
        }
        for (Session session : sessionList) {
            if(nickName.equals(session.getNickName())){
                return session;
            }
        }
        return null;
    }
}
