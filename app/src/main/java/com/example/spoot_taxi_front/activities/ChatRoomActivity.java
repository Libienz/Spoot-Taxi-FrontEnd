package com.example.spoot_taxi_front.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.spoot_taxi_front.models.ChatMessage;
import com.example.spoot_taxi_front.adapters.MessageAdapter;
import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.models.User;
import com.example.spoot_taxi_front.utils.SessionManager;
import com.example.spoot_taxi_front.utils.TestChatMessageGenerator;
import com.example.spoot_taxi_front.utils.WebSocketViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {
    private RecyclerView recyclerViewChat;
    private EditText editTextMessage;
    private Button buttonSend;
    private MessageAdapter messageAdapter;
    private WebSocketViewModel webSocketViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //
        webSocketViewModel = new ViewModelProvider(this).get(WebSocketViewModel.class);

        // 레이아웃 요소 초기화
        recyclerViewChat = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        // RecyclerView 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewChat.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapter();
        recyclerViewChat.setAdapter(messageAdapter);

        // 전송 버튼 클릭 리스너 설정
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 메시지 전송 처리
                String message = editTextMessage.getText().toString();
                sendMessage(message);
                editTextMessage.setText("");
            }
        });

        // 채팅 데이터 가져오기 (서버와의 연동 필요)
        List<ChatMessage> chatMessages = getChatMessages(); // 서버로부터 채팅 데이터 가져오기
        messageAdapter.setChatMessages(chatMessages);
    }

    private void sendMessage(String message) {
        // 메시지 전송 처리 (서버와의 연동 필요)
        // 서버로 메시지 전송
        JSONObject data = new JSONObject();

        // 내부 필드를 put하여 JSON 데이터를 생성합니다.
        try {
            data.put("senderEmail", SessionManager.getInstance().getCurrentUser().getEmail());
            data.put("senderName", SessionManager.getInstance().getCurrentUser().getNickname());
            data.put("sendTime", LocalDateTime.now().toString());
            data.put("message", message);

            // 중첩된 JSONObject를 생성하여 넣을 수도 있습니다.
            JSONObject chatRoom = new JSONObject();
            //이것도 fragment때 처럼 localhost:8080/api/chat/user/chatRooms에서 List까보고 내부의 "chatRoomId"가져오면 될듯 사실상 방 들어온순간 api날리게 해야겠네
            chatRoom.put("id", 1);
            data.put("chatRoom", chatRoom);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        webSocketViewModel.sendMessage(data);
    }

    private List<ChatMessage> getChatMessages() {
        // 채팅 데이터 가져오기 (서버와의 연동 필요)
        // 서버로부터 채팅 데이터 받아오기
        // 채팅 데이터를 ChatMessage 객체로 변환하여 반환

        //우선은 테스트용 데이터 리턴
        return TestChatMessageGenerator.generateChatMessages(); // 예시로 빈 리스트 반환
    }
}
