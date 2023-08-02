package com.example.spoot_taxi_front.network.dto;

import java.time.LocalDateTime;
import java.util.List;

public class UserJoinedChatRoomDto {
    private Long chatRoomId;
    private String chatRoomName;
    private String lastMessage;
    private LocalDateTime lastSentTime;
    private List<UserDto> participants;

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public LocalDateTime getLastSentTime() {
        return lastSentTime;
    }

    public List<UserDto> getParticipants() {
        return participants;
    }
}
