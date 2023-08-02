package com.example.spoot_taxi_front.network.dto.requests;

public class MatchCancelRequest {
    private String email;
    private Long waitingRoomId;
    private Long waitingRoomUserId;

    public MatchCancelRequest(String email, Long waitingRoomId, Long waitingRoomUserId) {
        this.email = email;
        this.waitingRoomId = waitingRoomId;
        this.waitingRoomUserId = waitingRoomUserId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getWaitingRoomId() {
        return waitingRoomId;
    }

    public void setWaitingRoomId(Long waitingRoomId) {
        this.waitingRoomId = waitingRoomId;
    }

    public Long getWaitingRoomUserId() {
        return waitingRoomUserId;
    }

    public void setWaitingRoomUserId(Long waitingRoomUserId) {
        this.waitingRoomUserId = waitingRoomUserId;
    }

}
