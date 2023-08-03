package com.example.spoot_taxi_front.utils;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.spoot_taxi_front.activities.ChatRoomActivity;
import com.example.spoot_taxi_front.models.ChatMessage;

import org.json.JSONException;
import org.json.JSONObject;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;


public class WebSocketManager {
    private static WebSocketManager instance;
    private StompClient stompClient;

    //웹소켓으로 오는 메세지를 실시간으로 관리하는 객체
    private MutableLiveData<ChatMessage> receivedMessages = new MutableLiveData<>();

    private WebSocketManager() {
        // Private constructor to prevent external instantiation
    }
    public static synchronized WebSocketManager getInstance() {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }

    // 웹소켓 메시지를 받아오는 콜백을 처리하는 메서드
    private void handleMessage(ChatMessage chatMessage) {
        receivedMessages.postValue(chatMessage); // LiveData의 값을 변경하여 옵저버들에게 알림
    }

    // LiveData를 반환하는 메서드
    public LiveData<ChatMessage> getReceivedMessages() {
        return receivedMessages;
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
            ChatMessage payloadToChatMessage = convertPayloadToChatMessage(topicMessage.getPayload());

            handleMessage(payloadToChatMessage);
            //Log.d("라이브데이터웹소켓",getReceivedMessages().getValue()); 이거 해놓으니까 null터지네;;
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
    private ChatMessage convertPayloadToChatMessage(String payload) {
        try {
            // Step 1: payload 문자열을 JSONObject로 파싱
            JSONObject jsonObject = new JSONObject(payload);

            // Step 2: 필드들의 값을 추출
            Long messageId = jsonObject.optLong("id");
            String senderName = jsonObject.optString("senderName");
            String message = jsonObject.optString("message");
            String senderId = jsonObject.optString("senderEmail");
            String sentTime = jsonObject.optString("sendTime");

            // Step 3: 추출한 값들을 사용하여 ChatMessage 객체 생성
            ChatMessage chatMessage = new ChatMessage(messageId, senderName, senderId, message, sentTime);

            return chatMessage;
        } catch (JSONException e) {
            e.printStackTrace();
            // JSON 파싱에 실패하면 예외가 발생하므로 null을 반환하거나 기본값으로 객체를 생성할 수 있습니다.
            return null;
        }
    }
}
