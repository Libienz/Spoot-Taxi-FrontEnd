package com.example.spoot_taxi_front;

import com.example.spoot_taxi_front.dto.Gender;

public class User {
    private String email;
    private String password;
    private String imgUrl;
    private Gender gender;

    public User(String email, String password, String imgUrl, Gender gender) {
        this.email = email;
        this.password = password;
        this.imgUrl = imgUrl;
        this.gender = gender;
    }

    // Getter and Setter methods

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}