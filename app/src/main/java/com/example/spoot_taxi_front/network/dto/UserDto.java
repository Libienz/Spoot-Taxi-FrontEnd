package com.example.spoot_taxi_front.network.dto;

import com.example.spoot_taxi_front.models.Gender;

import java.io.Serializable;

public class UserDto implements Serializable {
    private String email;
    private String password;
    private String name;
    private Gender gender;
    private String imgUrl;
    private String deviceToken;

    public UserDto(String email, String password, String name, Gender gender, String imgUrl) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.imgUrl = imgUrl;
    }

    public UserDto(String email, String password, String name, Gender gender) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.imgUrl = "http://192.168.219.109:8080/api/images/profile-image/default-profile-image.jpg";
        this.gender = gender;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", imgUri='" + imgUrl + '\'' +
                '}';
    }
}
