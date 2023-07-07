package com.example.spoot_taxi_front.utils;

import com.example.spoot_taxi_front.models.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private String jwtToken;

    private SessionManager() {
        // Private constructor to prevent instantiation from outside the class
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }



    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }
}
