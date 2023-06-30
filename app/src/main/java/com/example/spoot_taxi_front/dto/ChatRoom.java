package com.example.spoot_taxi_front.dto;

import java.util.List;

public class ChatRoom {
    private String roomId;
    private String roomName;
    private List<User> participants;
    private String lastMessage;
    private String lastSentTime;

    public ChatRoom(String roomId, String roomName, List<User> participants, String lastMessage, String lastSentTime) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.participants = participants;
        this.lastMessage = lastMessage;
        this.lastSentTime = lastSentTime;
    }

    public String getLastSentTime() {
        return lastSentTime;
    }

    public void setLastSentTime(String lastSentTime) {
        this.lastSentTime = lastSentTime;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }




}
