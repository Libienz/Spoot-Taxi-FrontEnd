package com.example.spoot_taxi_front.utils;

import com.example.spoot_taxi_front.dto.User;

public class CurrentUserHandler {
    private static CurrentUserHandler instance;
    private User currentUser;

    private CurrentUserHandler() {
        // Private constructor to prevent instantiation from outside the class
    }

    public static CurrentUserHandler getInstance() {
        if (instance == null) {
            synchronized (CurrentUserHandler.class) {
                if (instance == null) {
                    instance = new CurrentUserHandler();
                }
            }
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
