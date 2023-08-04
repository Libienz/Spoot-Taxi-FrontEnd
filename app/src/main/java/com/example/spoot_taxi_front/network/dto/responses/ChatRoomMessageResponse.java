package com.example.spoot_taxi_front.network.dto.responses;

import com.example.spoot_taxi_front.network.dto.MessageDto;

import java.util.List;

public class ChatRoomMessageResponse {
    private Boolean success;
    private String message;
    private Long chatParticipantId;
    private List<MessageDto> messageDtoList;

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Long getChatParticipantId() {
        return chatParticipantId;
    }

    public List<MessageDto> getMessageDtoList() {
        return messageDtoList;
    }
}
