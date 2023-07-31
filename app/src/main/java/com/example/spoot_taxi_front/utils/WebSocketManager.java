package com.example.spoot_taxi_front.utils;

import static android.content.ContentValues.TAG;

import android.util.Log;

import org.json.JSONObject;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class WebSocketManager {
    private StompClient stompClient;

    //원래는 ws://localhost:8080/ws/websocket 가 맞지만 안드로이드 에뮬레이터에서는 10.0.2.2 여야 하나봄. 실제폰으로는 아직 검증 안됨.
    public void connectWebSocket() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/ws/websocket");
        stompClient.connect();
    }

    public void subscribeToChannel(Long chatRoomId) {
        stompClient.topic("/sub/channel" + chatRoomId).subscribe(topicMessage -> {
            Log.d(TAG, topicMessage.getPayload());
        });
    }

    public void sendMessage(JSONObject data) {
        stompClient.send("/pub/send", data.toString()).subscribe();
    }

    public void disconnectWebSocket() {
        stompClient.disconnect();
    }
}
