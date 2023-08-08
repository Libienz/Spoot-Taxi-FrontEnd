package com.example.spoot_taxi_front.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.spoot_taxi_front.models.ChatMessage;
import com.example.spoot_taxi_front.adapters.MessageAdapter;
import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.network.api.ChatApi;
import com.example.spoot_taxi_front.network.dto.MessageDto;
import com.example.spoot_taxi_front.network.dto.responses.ChatRoomMessageResponse;
import com.example.spoot_taxi_front.network.dto.responses.LeaveChatParticipantResponse;
import com.example.spoot_taxi_front.network.dto.responses.UpdateChatParticipantResponse;
import com.example.spoot_taxi_front.network.retrofit.ApiClient;
import com.example.spoot_taxi_front.utils.LocalChatRoomManager;
import com.example.spoot_taxi_front.utils.SessionManager;
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
    private Button newMsgButton;
    private ImageButton buttonScrollToBottom;
    private MessageAdapter messageAdapter;
    private WebSocketViewModel webSocketViewModel;
    private Long chatRoomId;
    private Long chatParticipantId;
    private List<ChatMessage> chatMessageList = new ArrayList<>();

    private ChatApi chatApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //메뉴를 위한 툴바 설정, title을 false안하면 기본 디폴트타이틀이 보이게됨
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
        buttonScrollToBottom = findViewById(R.id.buttonScrollToBottom);
        newMsgButton = findViewById(R.id.newMsgButton);
        // RecyclerView 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerViewChat.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapter();
        recyclerViewChat.setAdapter(messageAdapter);
        //시작할 경우에 스크롤 버튼 꺼놓기
        buttonScrollToBottom.setVisibility(View.GONE);
        newMsgButton.setVisibility(View.GONE);
        recyclerViewChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) || recyclerView.getAdapter().getItemCount() == 0/*리사이클러 뷰에 아이템이 하나도 없는 경우*/) {

                    // 스크롤이 더 이상 안되는 경우 (맨 아래에 도달한 경우)
                    buttonScrollToBottom.setVisibility(View.GONE);
                    newMsgButton.setVisibility(View.GONE);
                } else {
                    // 그 외에는 버튼을 보이도록 설정
                    buttonScrollToBottom.setVisibility(View.VISIBLE);
                }
            }
        });

        //스크롤 버튼
        buttonScrollToBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewChat.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
            }
        });

        //newMsg알림 버튼
        newMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewChat.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
            }
        });
        // 전송 버튼 초기 상태를 비활성화로 설정
        buttonSend.setEnabled(false);

        // editText의 텍스트 변화를 감지하는 TextWatcher 설정
        editTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // editText의 텍스트가 비어있지 않은 경우에만 전송 버튼을 활성화
                if (s.toString().isEmpty()) {
                    buttonSend.setEnabled(false);
                } else {
                    buttonSend.setEnabled(true);
                }
            }
        });

        // 전송 버튼 클릭 리스너 설정
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 메시지 전송 처리
                String message = editTextMessage.getText().toString();
                sendMessage(message);
                editTextMessage.setText("");
                // 전송 후 다시 버튼 비활성화
                buttonSend.setEnabled(false);
            }
        });

        // 채팅 데이터 가져오기 (서버와의 연동 필요)
        chatApi = ApiClient.createChatApi();
        Call<ChatRoomMessageResponse> chatRoomMessages = chatApi.getChatRoomMessages(SessionManager.getInstance().getCurrentUser().getEmail(), chatRoomId);
        chatRoomMessages.enqueue(new Callback<ChatRoomMessageResponse>() {
            @Override
            public void onResponse(Call<ChatRoomMessageResponse> call, Response<ChatRoomMessageResponse> response) {
                handleChatRoomMessageResponse(response.code(), response.body());
                for (ChatMessage chatMessage : chatMessageList) {
                    Log.d("chatMessageList",chatMessage.toString());
                }
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
                    //현재 챗룸꺼인지 아이디로 확인
                    if(chatMessage.getChatRoomId()==chatRoomId) {
                        messageAdapter.addChatMessages(chatMessage);
                        if (chatMessage.getSenderId().equals(SessionManager.getInstance().getCurrentUser().getEmail())) {
                            recyclerViewChat.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                        } else {
                            if (isRecyclerViewAtBottom()) {
                                recyclerViewChat.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                            } else {
                                newMsgButton.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else {
                    // LiveData가 아직 메시지를 받지 않았거나 null 값을 가진 경우 처리하는 로직
                    Log.d("라이브리스트확인", "No message received yet");
                }
            }
        });
        //messageAdapter.setChatMessages(chatMessageList);
    }

    //나가기 버튼을 위해서 메뉴ActionBar inflate해줌
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chatroom_menu,menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 스크롤을 맨 아래로 이동
        scrollToBottom();
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatApi = ApiClient.createChatApi();
        Call<UpdateChatParticipantResponse> updateChatParticipantCall = chatApi.updateChatParticipant(chatParticipantId);
        updateChatParticipantCall.enqueue(new Callback<UpdateChatParticipantResponse>() {
            @Override
            public void onResponse(Call<UpdateChatParticipantResponse> call, Response<UpdateChatParticipantResponse> response) {
                if (response.code() != 200) {
                    Log.d("exitTimeUpdateApi", "fail");
                    Log.d("exitTimeUpdateApi", response.code()+"");

                }
            }

            @Override
            public void onFailure(Call<UpdateChatParticipantResponse> call, Throwable t) {

            }
        });
    }

    //메뉴 선택했을때 이벤트처리
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        //나가기
        if(itemId== R.id.action_leave){
            Toast.makeText(this,"채팅방을 나갔습니다.",Toast.LENGTH_SHORT).show();
            //leave API 사용
            Call<LeaveChatParticipantResponse> stringCall = chatApi.leaveChatParticipant(chatParticipantId);
            stringCall.enqueue(new Callback<LeaveChatParticipantResponse>() {
                @Override
                public void onResponse(Call<LeaveChatParticipantResponse> call, Response<LeaveChatParticipantResponse> response) {
                    Log.d("Leave API",response.body().getMessage());
                    webSocketViewModel.unsubscribeToChannel(chatRoomId);//채팅방 구독해제
                    sendExitMessage();
                    LocalChatRoomManager.getInstance().leaveChatRoom(chatRoomId);
                    onBackPressed();//나가고 뒤로가서(chatFragment로 가서) 채팅방을 나감
                }

                @Override
                public void onFailure(Call<LeaveChatParticipantResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "나가기 요청을 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("API Failure", "API 호출에 실패하였습니다.", t);
                }
            });

        }

        return super.onOptionsItemSelected(item);
    }

    private void handleChatRoomMessageResponse(int statusCode, ChatRoomMessageResponse responseBody) {
        switch (statusCode) {
            case 200:
                List<MessageDto> messageDtoList = responseBody.getMessageDtoList();
                //chatParticipant의 아이디도 액티비티에 저장해둔다(나가기 api쓸때 필요)
                chatParticipantId=responseBody.getChatParticipantId();
                Log.d("chatParticipantId는",chatParticipantId.toString());
                messageAdapter.setChatMessages(setChatMessageList(messageDtoList));// 어댑터를 여기서 set해야하는 이유는 밑에 주석부분에서 실행할시 비동기적으로 onCreate가 작동하기때문
                break;
            default:
                Toast.makeText(getApplicationContext(), "메시지 목록 정보를 받아올수 없습니다.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private List<ChatMessage> setChatMessageList(List<MessageDto> messageDtoList) {
        List<ChatMessage> chatMessageApiResponseList = new ArrayList<>();

        for (MessageDto messageDto : messageDtoList) {

            Long messageId = messageDto.getMessageId();
            String senderId = messageDto.getSenderId();
            String senderName = messageDto.getSenderName();
            String message = messageDto.getMessage();
            String senderProfileImageUrl = messageDto.getSenderProfileImageUrl();
            Boolean isSystem = messageDto.getSystem();
            //LocalDateTime sentTime = messageDto.getSentTime();

            Optional<LocalDateTime> optionalSentTime = Optional.ofNullable(messageDto.getSentTime());
            String sentTimeString = optionalSentTime.map(LocalDateTime::toString).orElse("");

            ChatMessage chatMessage = new ChatMessage(messageId, senderName, message, senderId, sentTimeString, senderProfileImageUrl,isSystem);
            chatMessageApiResponseList.add(chatMessage);
        }
        return chatMessageApiResponseList;
    }

    //입력한 텍스트와 필요한데이터를 json으로말아서 웹소켓이용해서 메시지 전송
    private void sendMessage(String message) {
        Log.d("SendMessage", "called");
        //현재 시간
        LocalDateTime now = LocalDateTime.now();
        // DateTimeFormatter를 사용하여 LocalDateTime을 원하는 형식으로 포맷합니다.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
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
            data.put("senderProfileImageUrl", SessionManager.getInstance().getCurrentUser().getImgUrl());

            // 중첩된 JSONObject를 생성하여 넣을 수도 있습니다.
            JSONObject chatRoom = new JSONObject();
            chatRoom.put("id", chatRoomId);
            data.put("chatRoom", chatRoom);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        webSocketViewModel.sendMessage(data);
    }
    //채팅방을 나가면서 서버에 메세지를 남김
    private void sendExitMessage(){
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = now.format(formatter);

        LocalDateTime parsedDateTime = LocalDateTime.parse(formattedDateTime, formatter);

        JSONObject data = new JSONObject();

        // 내부 필드를 put하여 JSON 데이터를 생성합니다.
        try {
            data.put("senderName", SessionManager.getInstance().getCurrentUser().getNickname());
            data.put("sendTime", parsedDateTime);

            // 중첩된 JSONObject를 생성하여 넣을 수도 있습니다.
            JSONObject chatRoom = new JSONObject();
            chatRoom.put("id", chatRoomId);
            data.put("chatRoom", chatRoom);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        webSocketViewModel.sendExitMessage(data);
    }

    // 리사이클러뷰가 현재 바닥에 있는지 확인하는 메소드
    private boolean isRecyclerViewAtBottom() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerViewChat.getLayoutManager();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        int itemCount = layoutManager.getItemCount();

        Log.d("lastVisibleItemPosition", lastVisibleItemPosition+"");
        Log.d("itemCount", itemCount+"");

        // 리사이클러뷰에 아이템이 없는 경우
        if (itemCount == 0) return true;

        // 현재 보이는 마지막 아이템이 전체에서 10번째 이내의 아이템이라면 바닥에 있다고 판단
        return lastVisibleItemPosition > (itemCount - 10);
    }

    private void scrollToBottom() {
        if (messageAdapter.getItemCount() > 0) {
            recyclerViewChat.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerViewChat.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                }
            }, 200);
        }
    }
}

