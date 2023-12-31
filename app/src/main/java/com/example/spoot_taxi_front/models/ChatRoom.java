package com.example.spoot_taxi_front.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatRoom implements Cloneable{
    private Long roomId;
    private String roomName;
    private List<User> participants;
    private String lastMessage;
    private String lastSentTime;
    private Integer nonReadMessageCount;

    public ChatRoom(Long roomId, String roomName, List<User> participants, String lastMessage, String lastSentTime, Integer nonReadMessageCount) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.participants = participants;
        this.lastMessage = lastMessage;
        this.lastSentTime = lastSentTime;
        this.nonReadMessageCount = nonReadMessageCount;
    }
    // clone 메서드를 오버라이딩하여 객체를 복사함
    @Override
    public ChatRoom clone() {
        try {
            ChatRoom copiedChatRoom = (ChatRoom) super.clone();
            // participants가 참조형이므로 새로운 리스트로 복사해야 함
            copiedChatRoom.participants = new ArrayList<>(this.participants);
            return copiedChatRoom;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "roomId=" + roomId +
                ", roomName='" + roomName + '\'' +
                ", participants=" + participants +
                ", lastMessage='" + lastMessage + '\'' +
                ", lastSentTime='" + lastSentTime + '\'' +
                '}';
    }

    public void setNonReadMessageCount(Integer nonReadMessageCount) {
        this.nonReadMessageCount = nonReadMessageCount;
    }

    public Integer getNonReadMessageCount() {
        return nonReadMessageCount;
    }

    // 현재 시간을 기준으로 정렬된 LocalDateTime 객체 반환
    public LocalDateTime getLastSentTimeToLocalDateTime() {
        // 여기서 LocalDateTime으로 변환하는 로직을 구현
        // 예시로는 아래와 같이 반환하도록 가정합니다.
        return LocalDateTime.parse(lastSentTime);
    }
    public String getLastSentTime() {
        return lastSentTime;
    }

    public void setLastSentTime(String lastSentTime) {
        this.lastSentTime = lastSentTime;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
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
