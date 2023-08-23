package com.example.spoot_taxi_front.utils;

import android.util.Log;

import com.example.spoot_taxi_front.models.ChatMessage;
import com.example.spoot_taxi_front.models.ChatRoom;
import com.example.spoot_taxi_front.models.User;
import com.example.spoot_taxi_front.network.api.ChatApi;
import com.example.spoot_taxi_front.network.dto.UserDto;
import com.example.spoot_taxi_front.network.dto.UserJoinedChatRoomDto;
import com.example.spoot_taxi_front.network.dto.responses.UserJoinedChatRoomResponse;
import com.example.spoot_taxi_front.network.retrofit.ApiManager;
import com.example.spoot_taxi_front.network.socket.WebSocketViewModel;

import org.greenrobot.eventbus.EventBus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocalChatRoomManager {

    private static LocalChatRoomManager instance;
    private List<ChatRoom> chatRooms = new ArrayList<>();
    private ChatApi chatApi;
    private WebSocketViewModel webSocketViewModel;

    private LocalChatRoomManager() {
        webSocketViewModel = WebSocketViewModel.getInstance();
        webSocketViewModel.connectWebSocket();
        chatApi = ApiManager.getInstance().createChatApi(SessionManager.getInstance().getJwtToken());
    }

    public static LocalChatRoomManager getInstance() {
        if (instance == null) {
            synchronized (LocalChatRoomManager.class) {
                if (instance == null) {
                    instance = new LocalChatRoomManager();
                }
            }
        }
        return instance;
    }

    public List<ChatRoom> getChatRooms() {
        return chatRooms;
    }
    public void setChatRooms(List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }

    public void newMessageArriveUpdate(ChatMessage chatMessage) {

        Long chatRoomId = chatMessage.getChatRoomId();
        String message = chatMessage.getMessage();
        String sentTime = chatMessage.getSentTime();

        for (int i = 0; i < chatRooms.size(); i++) {
            ChatRoom chatRoom = chatRooms.get(i);
            if (chatRoom.getRoomId() == chatRoomId) {
                if (!chatMessage.getSenderId().equals(SessionManager.getInstance().getCurrentUser().getEmail())) {
                    chatRoom.setNonReadMessageCount(chatRoom.getNonReadMessageCount() + 1);
                }
                chatRoom.setLastMessage(message);
                chatRoom.setLastSentTime(sentTime);
                break;
            }
        }

        EventBus.getDefault().post(new ChatRoomDataChange());
    }

    public void nonReadCountZeroUpdate(Long chatRoomId) {
        for (int i = 0; i < chatRooms.size(); i++) {
            ChatRoom chatRoom = chatRooms.get(i);
            if (chatRoom.getRoomId() == chatRoomId) {
                chatRoom.setNonReadMessageCount(0);
                break;
            }
        }

        EventBus.getDefault().post(new ChatRoomDataChange());
    }

    public void exitChatRoom(Long chatRoomId) {
        for (int i = 0; i < chatRooms.size(); i++) {
            ChatRoom chatRoom = chatRooms.get(i);
            if (chatRoom.getRoomId() == chatRoomId) {
                chatRooms.remove(i);
                break;
            }
        }

        EventBus.getDefault().post(new ChatRoomDataChange());
    }

    public int getTotalNonReadCount() {
        int res = 0;
        for (int i = 0; i < chatRooms.size(); i++) {
            ChatRoom chatRoom = chatRooms.get(i);
            res += chatRoom.getNonReadMessageCount();
        }
        return res;
    }

}
