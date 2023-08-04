package com.example.spoot_taxi_front.models;

public class ChatMessage {
    private Long messageId;
    private String senderName;
    private String message;

    private String senderId;
    private String sentTime;

    private Long chatRoomId;

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

    public ChatMessage(Long messageId, String senderName, String senderId, String message, String sentTime) {
        this.messageId = messageId;
        this.senderName = senderName;
        this.senderId = senderId;
        this.message = message;
        this.sentTime = sentTime;
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
}
