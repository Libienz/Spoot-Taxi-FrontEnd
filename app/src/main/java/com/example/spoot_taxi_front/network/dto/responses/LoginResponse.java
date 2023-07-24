package com.example.spoot_taxi_front.network.dto.responses;

import com.example.spoot_taxi_front.network.dto.UserDto;

public class LoginResponse {
    private Boolean success;
    private String message;
    private String token;
    private UserDto userDto;

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public UserDto getUserDto() {
        return userDto;
    }
}
