package com.example.spoot_taxi_front;

public class ChatMessage {
    private String messageId;
    private String senderName;
    private String message;
    private long sentTime;

    public ChatMessage(String messageId, String senderName, String message, Long sentTime) {
        this.messageId = messageId;
        this.senderName = senderName;
        this.message = message;
        this.sentTime = sentTime;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
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

    public long getSentTime() {
        return sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }
}
