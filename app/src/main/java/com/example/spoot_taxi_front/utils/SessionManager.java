package com.example.spoot_taxi_front.utils;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.spoot_taxi_front.activities.MainActivity;
import com.example.spoot_taxi_front.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private String jwtToken;

    private String deviceToken;
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


    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceToken() {
        return deviceToken;
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
