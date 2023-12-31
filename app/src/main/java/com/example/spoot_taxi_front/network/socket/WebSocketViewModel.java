package com.example.spoot_taxi_front.network.socket;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.spoot_taxi_front.models.ChatMessage;

import org.json.JSONObject;

import ua.naiksoftware.stomp.StompClient;

public class WebSocketViewModel extends ViewModel {
    private static WebSocketViewModel instance;
    private WebSocketManager webSocketManager = WebSocketManager.getInstance();

    public static synchronized WebSocketViewModel getInstance() {
        if (instance == null) {
            instance = new WebSocketViewModel();
        }
        return instance;
    }

    public void reconnect(){
        webSocketManager.reconnect();
    }
    public boolean isConnected(){
        return webSocketManager.isConnected();
    }
    public StompClient getStompClient() {
        return webSocketManager.getStompClient();
    }

    public void connectWebSocket() {
        webSocketManager.connectWebSocket();
    }

    public void subscribeToChannel(Long chatRoomId) {
        webSocketManager.subscribeToChannel(chatRoomId);
    }
    public void unsubscribeToChannel(Long chatRoomId) {
        webSocketManager.unsubscribeToChannel(chatRoomId);
    }
    public void sendMessage(JSONObject data) {
        webSocketManager.sendMessage(data);
    }
    public void sendExitMessage(JSONObject data) {
        webSocketManager.sendExitMessage(data);
    }
    public void disconnectWebSocket() {
        webSocketManager.disconnectWebSocket();
    }
    // WebSocketManager의 MutableLiveData를 반환
    public LiveData<ChatMessage> getReceivedMessages() {
        return webSocketManager.getReceivedMessages();
    }
}
