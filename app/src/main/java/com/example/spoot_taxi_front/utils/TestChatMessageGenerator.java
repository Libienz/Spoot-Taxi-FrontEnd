package com.example.spoot_taxi_front.utils;

import com.example.spoot_taxi_front.dto.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class TestChatMessageGenerator {

    public static List<ChatMessage> generateChatMessages() {
        List<ChatMessage> chatMessages = new ArrayList<>();

        // 테스트용 데이터 추가
        ChatMessage message1 = new ChatMessage("1","user1", "Hello!", System.currentTimeMillis());
        ChatMessage message2 = new ChatMessage("2","user2", "Hi there!", System.currentTimeMillis() + 1000);
        ChatMessage message3 = new ChatMessage("3","user1", "How are you?", System.currentTimeMillis() + 2000);
        ChatMessage message4 = new ChatMessage("4","user2", "I'm good, thanks!", System.currentTimeMillis() + 3000);
        ChatMessage message5 = new ChatMessage("5","user1", "Great!", System.currentTimeMillis() + 4000);

        // 리스트에 추가
        chatMessages.add(message1);
        chatMessages.add(message2);
        chatMessages.add(message3);
        chatMessages.add(message4);
        chatMessages.add(message5);

        return chatMessages;
    }
}
