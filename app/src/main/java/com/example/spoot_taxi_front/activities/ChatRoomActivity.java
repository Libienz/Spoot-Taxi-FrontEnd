package com.example.spoot_taxi_front.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.spoot_taxi_front.dto.ChatMessage;
import com.example.spoot_taxi_front.adapters.MessageAdapter;
import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.utils.TestChatMessageGenerator;

import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {
    private RecyclerView recyclerViewChat;
    private EditText editTextMessage;
    private Button buttonSend;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

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
    }

    private List<ChatMessage> getChatMessages() {
        // 채팅 데이터 가져오기 (서버와의 연동 필요)
        // 서버로부터 채팅 데이터 받아오기
        // 채팅 데이터를 ChatMessage 객체로 변환하여 반환

        //우선은 테스트용 데이터 리턴
        return TestChatMessageGenerator.generateChatMessages(); // 예시로 빈 리스트 반환
    }
}
