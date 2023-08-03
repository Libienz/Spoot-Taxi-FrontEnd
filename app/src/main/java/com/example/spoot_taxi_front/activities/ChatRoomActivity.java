package com.example.spoot_taxi_front.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.spoot_taxi_front.models.ChatMessage;
import com.example.spoot_taxi_front.adapters.MessageAdapter;
import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.models.User;
import com.example.spoot_taxi_front.network.api.ChatApi;
import com.example.spoot_taxi_front.network.dto.MessageDto;
import com.example.spoot_taxi_front.network.dto.UserJoinedChatRoomDto;
import com.example.spoot_taxi_front.network.dto.responses.ChatRoomMessageResponse;
import com.example.spoot_taxi_front.network.retrofit.ApiClient;
import com.example.spoot_taxi_front.utils.SessionManager;
import com.example.spoot_taxi_front.utils.TestChatMessageGenerator;
import com.example.spoot_taxi_front.utils.WebSocketViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoomActivity extends AppCompatActivity {
    private RecyclerView recyclerViewChat;
    private EditText editTextMessage;
    private Button buttonSend;
    private MessageAdapter messageAdapter;
    private WebSocketViewModel webSocketViewModel;
    private Long chatRoomId;
    private List<ChatMessage> chatMessageList = new ArrayList<>();

    private ChatApi chatApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        // -1은 기본값
        chatRoomId = getIntent().getLongExtra("chatRoomId", -1);
        Log.d("채팅방 id", chatRoomId.toString());

        //웹소켓매니저 가져오기
        //webSocketViewModel = new ViewModelProvider(this).get(WebSocketViewModel.class);
        webSocketViewModel = WebSocketViewModel.getInstance();

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
        chatApi = ApiClient.createChatApi();
        Call<ChatRoomMessageResponse> chatRoomMessages = chatApi.getChatRoomMessages(SessionManager.getInstance().getCurrentUser().getEmail(), chatRoomId);
        chatRoomMessages.enqueue(new Callback<ChatRoomMessageResponse>() {
            @Override
            public void onResponse(Call<ChatRoomMessageResponse> call, Response<ChatRoomMessageResponse> response) {
                chatMessageList = handleChatRoomMessageResponse(response.code(), response.body());
                messageAdapter.setChatMessages(chatMessageList);// 어댑터를 여기서 set해야하는 이유는 밑에 주석부분에서 실행할시 비동기적으로 onCreate가 작동하기때문
            }

            @Override
            public void onFailure(Call<ChatRoomMessageResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "메시지 목록 요청에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e("API Failure", "API 호출에 실패하였습니다.", t);
            }
        });

        // WebSocketViewModel의 LiveData를 관찰하여 UI를 업데이트
        webSocketViewModel.getReceivedMessages().observe(this, new Observer<ChatMessage>() {
            @Override
            public void onChanged(ChatMessage chatMessage) {
                if (chatMessage != null) {
                    // LiveData에서 유효한 메시지를 받았을 때 처리하는 로직
                    Log.d("라이브리스트확인", String.valueOf(chatMessage));
                    chatMessageList.add(chatMessage);
                    messageAdapter.setChatMessages(chatMessageList);
                } else {
                    // LiveData가 아직 메시지를 받지 않았거나 null 값을 가진 경우 처리하는 로직
                    Log.d("라이브리스트확인", "No message received yet");
                }
            }
        });
        //messageAdapter.setChatMessages(chatMessageList);
    }

    private List<ChatMessage> handleChatRoomMessageResponse(int statusCode, ChatRoomMessageResponse responseBody) {
        switch (statusCode) {
            case 200:
                List<MessageDto> messageDtoList = responseBody.getMessageDtoList();
                return setChatMessageList(messageDtoList);
            default:
                Toast.makeText(getApplicationContext(), "메시지 목록 정보를 받아올수 없습니다.", Toast.LENGTH_SHORT).show();
                break;
        }
        return null;
    }

    private List<ChatMessage> setChatMessageList(List<MessageDto> messageDtoList) {
        List<ChatMessage> chatMessageApiResponseList = new ArrayList<>();

        for (MessageDto messageDto : messageDtoList) {

            Long messageId = messageDto.getMessageId();
            String senderId = messageDto.getSenderId();
            String senderName = messageDto.getSenderName();
            String message = messageDto.getMessage();

            //LocalDateTime sentTime = messageDto.getSentTime();

            Optional<LocalDateTime> optionalSentTime = Optional.ofNullable(messageDto.getSentTime());
            String sentTimeString = optionalSentTime.map(LocalDateTime::toString).orElse("");
            ChatMessage chatMessage = new ChatMessage(messageId, senderName, senderId, message, sentTimeString);
            chatMessageApiResponseList.add(chatMessage);
        }
        return chatMessageApiResponseList;
    }

    private void sendMessage(String message) {
        // 메시지 전송 처리 (서버와의 연동 필요)
        // 서버로 메시지 전송

        LocalDateTime now = LocalDateTime.now();
        // DateTimeFormatter를 사용하여 LocalDateTime을 원하는 형식으로 포맷합니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
//        System.out.println("포맷된 날짜와 시간: " + formattedDateTime);

        // DateTimeFormatter를 사용하여 formattedDateTime을 다시 LocalDateTime으로 파싱합니다.
        LocalDateTime parsedDateTime = LocalDateTime.parse(formattedDateTime, formatter);
//        System.out.println("파싱된 LocalDateTime: " + parsedDateTime);

        JSONObject data = new JSONObject();

        // 내부 필드를 put하여 JSON 데이터를 생성합니다.
        try {
            data.put("senderEmail", SessionManager.getInstance().getCurrentUser().getEmail());
            data.put("senderName", SessionManager.getInstance().getCurrentUser().getNickname());
            data.put("sendTime", parsedDateTime);
            data.put("message", message);

            // 중첩된 JSONObject를 생성하여 넣을 수도 있습니다.
            JSONObject chatRoom = new JSONObject();
            chatRoom.put("id", chatRoomId);
            data.put("chatRoom", chatRoom);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        webSocketViewModel.sendMessage(data);
    }
}
/*    private List<ChatMessage> getChatMessages() {
        // 채팅 데이터 가져오기 (서버와의 연동 필요)
        // 서버로부터 채팅 데이터 받아오기
        // 채팅 데이터를 ChatMessage 객체로 변환하여 반환

        //우선은 테스트용 데이터 리턴
        return TestChatMessageGenerator.generateChatMessages(); // 예시로 빈 리스트 반환
    }*/

