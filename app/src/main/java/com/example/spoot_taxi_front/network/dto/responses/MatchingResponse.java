package com.example.spoot_taxi_front.network.dto.responses;

public class MatchingResponse {
    private Boolean success;
    private String message;
    private Long waitingRoomId;
    private Long waitingRoomUserId;

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Long getWaitingRoomId() {
        return waitingRoomId;
    }

    public Long getWaitingRoomUserId() {
        return waitingRoomUserId;
    }
}
