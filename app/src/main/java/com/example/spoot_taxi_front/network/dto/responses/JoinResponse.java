package com.example.spoot_taxi_front.network.dto.responses;

import com.example.spoot_taxi_front.network.dto.UserDto;

public class JoinResponse {

    private Boolean success;
    private String message;
    UserDto userDto;
    String token;

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public String getToken() {
        return token;
    }
}
