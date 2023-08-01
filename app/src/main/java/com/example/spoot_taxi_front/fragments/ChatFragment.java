package com.example.spoot_taxi_front.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.utils.TestChatRoomGenerator;
import com.example.spoot_taxi_front.adapters.ChatRoomAdapter;
import com.example.spoot_taxi_front.utils.WebSocketViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class ChatFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private ChatRoomAdapter chatRoomAdapter;
    private StompClient mStompClient;

    private WebSocketViewModel webSocketViewModel;
    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // RecyclerView 초기화
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        chatRoomAdapter = new ChatRoomAdapter(TestChatRoomGenerator.generateChatRooms()); // 유저가 참여중인 채팅방 리스트 api로 받아오고 넣어야 함 지금은 테스트 데이터 넣은 것
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatRecyclerView.setAdapter(chatRoomAdapter);

        webSocketViewModel = new ViewModelProvider(this).get(WebSocketViewModel.class);

        // WebSocket 연결
        webSocketViewModel.connectWebSocket();
        // 특정 채널 구독
        // 구독하려면 아마 localhost:8080/api/chat/user/chatRooms에서 List까보고 내부의 "chatRoomId"가져오면 될듯
        webSocketViewModel.subscribeToChannel(1L);

        return view;
    }

    public void initStomp(){

        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/ws/websocket");
        Log.d("소켓연결","소켓연결");
        mStompClient.connect();
        Log.d("소켓연결후","소켓연결후");

        mStompClient.topic("/sub/channel/1").subscribe(topicMessage -> {
            Log.d(TAG, topicMessage.getPayload());
        });
        Log.d("구독후","구독후");
        JSONObject data = new JSONObject();

        // 내부 필드를 put하여 JSON 데이터를 생성합니다.
        try {
            data.put("senderEmail", "android");
            data.put("senderName", "test1");
            data.put("sendTime", "time1");
            data.put("message", "its me android!");

            // 중첩된 JSONObject를 생성하여 넣을 수도 있습니다.
            JSONObject chatRoom = new JSONObject();
            chatRoom.put("id", 1);
            data.put("chatRoom", chatRoom);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Log.d("메세지 발송","발송");
        mStompClient.send("/pub/send", data.toString()).subscribe();
        Log.d("발송후","발송후");
    }

}
