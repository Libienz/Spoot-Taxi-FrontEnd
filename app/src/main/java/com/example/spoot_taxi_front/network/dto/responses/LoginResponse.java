package com.example.spoot_taxi_front.network.dto.responses;

import com.example.spoot_taxi_front.network.dto.UserDto;

public class LoginResponse {
    private String token;
    private UserDto userDto;
    private String error;

    public String getToken() {
        return token;
    }


    public UserDto getUserDto() {
        return userDto;
    }

    public String getError() {
        return error;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", userDto=" + userDto +
                ", error='" + error + '\'' +
                '}';
    }
}
