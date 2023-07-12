package com.example.spoot_taxi_front.network.dto.responses;

import com.example.spoot_taxi_front.network.dto.UserDto;

public class JoinResponse {


    UserDto userDto;
    String token;

    public UserDto getUserDto() {
        return userDto;
    }

    public String getToken() {
        return token;
    }
}
