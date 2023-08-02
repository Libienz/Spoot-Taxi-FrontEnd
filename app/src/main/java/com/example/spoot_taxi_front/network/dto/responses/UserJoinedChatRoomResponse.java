package com.example.spoot_taxi_front.network.dto.responses;

import com.example.spoot_taxi_front.network.dto.UserJoinedChatRoomDto;

import java.util.List;

public class UserJoinedChatRoomResponse {
    private Boolean success;
    private String message;
    private List<UserJoinedChatRoomDto> userJoinedChatRoomDtoList;
    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<UserJoinedChatRoomDto> getUserJoinedChatRoomDtoList() {
        return userJoinedChatRoomDtoList;
    }
}
