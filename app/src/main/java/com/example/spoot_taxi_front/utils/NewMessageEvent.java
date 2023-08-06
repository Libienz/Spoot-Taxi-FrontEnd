package com.example.spoot_taxi_front.utils;

import com.example.spoot_taxi_front.models.ChatMessage;

public class NewMessageEvent {
    ChatMessage chatMessage;

    public NewMessageEvent(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }
}
