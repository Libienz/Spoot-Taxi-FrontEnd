package com.example.spoot_taxi_front.network.dto;

import java.time.LocalDateTime;

public class MessageDto {
    private Long messageId;
    private String senderId;
    private String senderName;
    private String message;
    private LocalDateTime sentTime;

    public Long getMessageId() {
        return messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }
}
