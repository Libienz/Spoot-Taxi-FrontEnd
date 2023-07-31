package com.example.spoot_taxi_front.utils;

import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

public class WebSocketViewModel extends ViewModel {
    private WebSocketManager webSocketManager = new WebSocketManager();

    public void connectWebSocket() {
        webSocketManager.connectWebSocket();
    }

    public void subscribeToChannel(Long chatRoomId) {
        webSocketManager.subscribeToChannel(chatRoomId);
    }

    public void sendMessage(JSONObject data) {
        webSocketManager.sendMessage(data);
    }

    public void disconnectWebSocket() {
        webSocketManager.disconnectWebSocket();
    }
}
