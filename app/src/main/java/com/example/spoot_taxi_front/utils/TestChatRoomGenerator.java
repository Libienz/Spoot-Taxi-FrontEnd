package com.example.spoot_taxi_front.utils;


import com.example.spoot_taxi_front.models.ChatRoom;
import com.example.spoot_taxi_front.models.User;
import com.example.spoot_taxi_front.models.Gender;

import java.util.ArrayList;
import java.util.List;

public class TestChatRoomGenerator {

    public static List<ChatRoom> generateChatRooms() {
        List<ChatRoom> chatRooms = new ArrayList<>();

        // Create users
        User user1 = new User("user1@example.com", "password1", "profile1", Gender.FEMALE);
        User user2 = new User("user2@example.com", "password2", "profile2", Gender.MALE);
        User user3 = new User("user3@example.com", "password3", "profile3", Gender.ETC);

        // Create chat rooms
        ChatRoom chatRoom1 = new ChatRoom("room1", "Chat Room 1", List.of(user1, user2), "Last message 1", "10:30 AM");
        ChatRoom chatRoom2 = new ChatRoom("room2", "Chat Room 2", List.of(user1, user3), "Last message 2","10:31 AM");
        ChatRoom chatRoom3 = new ChatRoom("room3", "Chat Room 3", List.of(user2, user3), "Last message 3","10:32 AM");
        ChatRoom chatRoom4 = new ChatRoom("room4", "Chat Room 4", List.of(user1), "Last message 4","10:33 AM");
        ChatRoom chatRoom5 = new ChatRoom("room5", "Chat Room 5", List.of(user3), "Last message 5","10:34 AM");

        // Add chat rooms to the list
        chatRooms.add(chatRoom1);
        chatRooms.add(chatRoom2);
        chatRooms.add(chatRoom3);
        chatRooms.add(chatRoom4);
        chatRooms.add(chatRoom5);

        return chatRooms;
    }
}
