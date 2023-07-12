package com.example.spoot_taxi_front.network.dto.responses;

import com.example.spoot_taxi_front.network.dto.UserDto;

public class UserSaveResponse {
    private Boolean success;
    private String message;
    private UserDto userDto;

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public UserDto getUserDto() {
        return userDto;
    }
}
