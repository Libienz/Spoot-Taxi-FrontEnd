package com.example.spoot_taxi_front.models;

import com.example.spoot_taxi_front.R;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String password;
    private String imgUrl;
    private String nickname;
    private Gender gender;

    public User(String email, String password, String nickname, String imgUrl, Gender gender) {
        this.email = email;
        this.password = password;
        this.imgUrl = imgUrl; //기본 이미지로 설정
        this.nickname = nickname;
        this.gender = gender;
    }

    public User(String email, String password, String nickname, Gender gender) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.imgUrl = "http://192.168.219.109:8080/images/default-profile-image";
        this.gender = gender;
    }
    // Getter and Setter methods


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

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