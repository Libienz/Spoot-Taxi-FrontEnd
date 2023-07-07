package com.example.spoot_taxi_front.models;

import com.example.spoot_taxi_front.R;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String password;
    private String imgUri;
    private String nickname;
    private Gender gender;

    public User(String email, String password, String nickname, Gender gender) {
        this.email = email;
        this.password = password;

        // img_default_profile의 리소스 ID 가져오기
        int defaultProfileResId = R.drawable.img_default_profile;
        // 리소스 ID를 통해 URI 생성
        String defaultProfileUri = "android.resource://" + "com.example.spoot_taxi_front" + "/" + defaultProfileResId;
        this.imgUri = defaultProfileUri; //기본 이미지로 설정
        this.nickname = nickname;
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

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}