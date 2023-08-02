package com.example.spoot_taxi_front.utils;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.spoot_taxi_front.activities.ChatRoomActivity;

import org.json.JSONObject;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;


public class WebSocketManager {
    private static WebSocketManager instance;
    private StompClient stompClient;

    private WebSocketManager() {
        // Private constructor to prevent external instantiation
    }
    public static synchronized WebSocketManager getInstance() {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }
    //원래는 ws://localhost:8080/ws/websocket 가 맞지만 안드로이드 에뮬레이터에서는 10.0.2.2 여야 하나봄. 실제폰으로는 아직 검증 안됨.
    public StompClient connectWebSocket() {
        if (stompClient == null) {
            stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/ws/websocket");
            stompClient.connect();
        }

        return stompClient;
    }

    public void subscribeToChannel(Long chatRoomId) {
        stompClient.topic("/sub/channel/" + chatRoomId).subscribe(topicMessage -> {
            Log.d("TAG", topicMessage.getPayload());
        });
    }

    public void sendMessage(JSONObject data) {
        stompClient.send("/pub/send", data.toString()).subscribe();
    }

    public void disconnectWebSocket() {
        if (stompClient != null && stompClient.isConnected()) {
            stompClient.disconnect();
            stompClient = null;
        }
    }
}
