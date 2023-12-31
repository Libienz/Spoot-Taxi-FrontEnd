package com.example.spoot_taxi_front.models;

public class ChatMessage {
    private Long messageId;
    private String senderName;
    private String message;
    private String senderId;
    private String sentTime;
    private Long chatRoomId;
    private String senderProfileImageUrl;
    private Boolean isSystem;
    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "messageId='" + messageId + '\'' +
                ", senderName='" + senderName + '\'' +
                ", message='" + message + '\'' +
                ", senderId='" + senderId + '\'' +
                ", sentTime=" + sentTime +
                '}';
    }

    public ChatMessage(Long messageId, String senderName, String message, String senderId, String sentTime, Long chatRoomId, String senderProfileImageUrl, Boolean isSystem) {
        this.messageId = messageId;
        this.senderName = senderName;
        this.message = message;
        this.senderId = senderId;
        this.sentTime = sentTime;
        this.chatRoomId = chatRoomId;
        this.senderProfileImageUrl = senderProfileImageUrl;
        this.isSystem = isSystem;
    }

    public ChatMessage(Long messageId, String senderName, String message, String senderId, String sentTime, String senderProfileImageUrl,Boolean isSystem) {
        this.messageId = messageId;
        this.senderName = senderName;
        this.message = message;
        this.senderId = senderId;
        this.sentTime = sentTime;
        this.senderProfileImageUrl = senderProfileImageUrl;
        this.isSystem = isSystem;
    }

    public String getSenderProfileImageUrl() {
        return senderProfileImageUrl;
    }

    public void setSenderProfileImageUrl(String senderProfileImageUrl) {
        this.senderProfileImageUrl = senderProfileImageUrl;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    public Boolean getSystem() {
        return isSystem;
    }

    public void setSystem(Boolean system) {
        isSystem = system;
    }
}
