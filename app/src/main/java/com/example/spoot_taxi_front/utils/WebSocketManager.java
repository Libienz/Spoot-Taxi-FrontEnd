package com.example.spoot_taxi_front.utils;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.spoot_taxi_front.models.ChatMessage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;


public class WebSocketManager {
    private static WebSocketManager instance;
    private StompClient stompClient;

    //웹소켓으로 오는 메세지를 실시간으로 관리하는 객체
    private MutableLiveData<ChatMessage> receivedMessages = new MutableLiveData<>();

    // 구독한 주소와 해당 구독의 Disposable을 매핑하는 Map을 사용
    Map<String, Disposable> subscriptionMap = new HashMap<>();
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
        Log.d("핸들메세지","잘되나");
        receivedMessages.postValue(chatMessage); // LiveData의 값을 변경하여 옵저버들에게 알림
        if (chatMessage.getSenderId().equals(SessionManager.getInstance().getCurrentUser().getEmail())) {
            return;
        }
        EventBus.getDefault().post(new NewMessageEvent(chatMessage));
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
        // 구독 시도하는 부분
        String addressToSubscribe = "/sub/channel/" + chatRoomId;
        if (!subscriptionMap.containsKey(addressToSubscribe)) {
            // 아직 구독되지 않은 주소라면 구독 처리
            Disposable chatRoomSubscription = stompClient.topic(addressToSubscribe).subscribe(topicMessage -> {
                Log.d("messageArrived", topicMessage.getPayload());
                ChatMessage payloadToChatMessage = convertPayloadToChatMessage(topicMessage.getPayload());
                handleMessage(payloadToChatMessage);
            });
            Log.d("disposable 확인", String.valueOf(chatRoomSubscription.isDisposed()));//false가 나와야 정상
            subscriptionMap.put(addressToSubscribe,chatRoomSubscription);
            Log.d("구독된거Map에넣기", String.valueOf(subscriptionMap.containsKey(addressToSubscribe)));
/*            // 구독 처리 후 Set에 주소 추가
            subscribedAddresses.add(addressToSubscribe);*/
        } else {
            // 이미 구독 중인 주소라면 처리를 중복 수행하지 않도록 로그 등의 방어적인 처리
            Log.d("Already Sub", "Already subscribed to address: " + addressToSubscribe);
        }
    }

    public void unsubscribeToChannel(Long chatRoomId){
        String addressToUnsubscribe = "/sub/channel/" + chatRoomId;
        if(subscriptionMap.containsKey(addressToUnsubscribe)){
            Disposable disposable = subscriptionMap.get(addressToUnsubscribe);
            disposable.dispose(); //구독해제
            if(disposable.isDisposed()){
                Log.d("구독해제됨","ㅇㅇ");
            }else {
                Log.d("구독해제안됨","망함");
            }
            subscriptionMap.remove(addressToUnsubscribe);
        }
        else {
            // 구독 중이 아닌 주소라면 아니라고 로그찍음
            Log.d("Not Sub", "Not subscribed to address: " + addressToUnsubscribe);
        }
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
            String senderProfileImageUrl = jsonObject.optString("senderProfileImageUrl");
            // chatRoom에 해당하는 JSON 객체를 가져옴
            JSONObject chatRoomObject = jsonObject.getJSONObject("chatRoom");
            // chatRoom의 id에 해당하는 값 가져오기
            Long chatRoomId = chatRoomObject.getLong("id");
            // Step 3: 추출한 값들을 사용하여 ChatMessage 객체 생성
            ChatMessage chatMessage = new ChatMessage(messageId, senderName, message, senderId, sentTime, chatRoomId, senderProfileImageUrl);

            return chatMessage;
        } catch (JSONException e) {
            e.printStackTrace();
            // JSON 파싱에 실패하면 예외가 발생하므로 null을 반환하거나 기본값으로 객체를 생성할 수 있습니다.
            return null;
        }
    }
}
