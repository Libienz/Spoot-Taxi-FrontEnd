package com.example.spoot_taxi_front.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spoot_taxi_front.R;
import com.example.spoot_taxi_front.models.ChatRoom;
import com.example.spoot_taxi_front.models.User;
import com.example.spoot_taxi_front.network.api.ChatApi;
import com.example.spoot_taxi_front.network.dto.UserDto;
import com.example.spoot_taxi_front.network.dto.UserJoinedChatRoomDto;
import com.example.spoot_taxi_front.network.dto.responses.UserJoinedChatRoomResponse;
import com.example.spoot_taxi_front.network.retrofit.ApiManager;
import com.example.spoot_taxi_front.utils.SessionManager;
import com.example.spoot_taxi_front.adapters.ChatRoomAdapter;
import com.example.spoot_taxi_front.utils.WebSocketViewModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.StompClient;

public class ChatFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private ChatRoomAdapter chatRoomAdapter;
    private StompClient mStompClient;

    private WebSocketViewModel webSocketViewModel;

    private ChatApi chatApi;
    private List<ChatRoom> chatRoomList = new ArrayList<>();

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        //webSocketViewModel = new ViewModelProvider(this).get(WebSocketViewModel.class);
        webSocketViewModel = WebSocketViewModel.getInstance();

        // WebSocket 연결
        webSocketViewModel.connectWebSocket();

        chatApi= ApiManager.createChatApi(SessionManager.getInstance().getJwtToken());
        loadChatRoomList(view);

        return view;
    }

    public void onResume() {
        super.onResume();
        Log.d("onResume","onResume실행");
        View view = getView();
        loadChatRoomList(view);
    }

    public void loadChatRoomList(View view) {
        Log.d("loadChatRoomList실행","loadChatRoomList실행중");
        Call<UserJoinedChatRoomResponse> call = chatApi.getUserChatRooms(SessionManager.getInstance().getCurrentUser().getEmail());

        // RecyclerView 초기화
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatRoomAdapter = new ChatRoomAdapter();
        chatRecyclerView.setAdapter(chatRoomAdapter);

        call.enqueue(new Callback<UserJoinedChatRoomResponse>() {
            @Override
            public void onResponse(Call<UserJoinedChatRoomResponse> call, Response<UserJoinedChatRoomResponse> response) {
                chatRoomList=handleUserJoinedChatRoomResponse(response.code(), response.body());
                chatRoomAdapter.setChatRoomAdapter(chatRoomList);// 유저가 참여중인 채팅방 리스트 api로 받은값 set
            }

            @Override
            public void onFailure(Call<UserJoinedChatRoomResponse> call, Throwable t) {
                Toast.makeText(getContext(), "채팅방 목록 요청에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                Log.e("API Failure", "API 호출에 실패하였습니다.", t);
            }
        });

    }

    private List<ChatRoom> handleUserJoinedChatRoomResponse(int statusCode, UserJoinedChatRoomResponse responseBody) {
        switch (statusCode) {
            case 200:
                List<UserJoinedChatRoomDto> userJoinedChatRoomDtoList = responseBody.getUserJoinedChatRoomDtoList();
                return setChatRoomList(userJoinedChatRoomDtoList);
            default:
                Toast.makeText(getContext(), "채팅방 목록 정보를 받아올수 없습니다.", Toast.LENGTH_SHORT).show();
                break;
        }
        return null;
    }

    private List<ChatRoom> setChatRoomList(List<UserJoinedChatRoomDto> userJoinedChatRoomDtoList) {
        List<ChatRoom> chatRoomApiResponseList = new ArrayList<>();
        for (UserJoinedChatRoomDto userJoinedChatRoomDto : userJoinedChatRoomDtoList) {
            Long chatRoomId = userJoinedChatRoomDto.getChatRoomId();
            String chatRoomName = userJoinedChatRoomDto.getChatRoomName();

            List<User> userList = new ArrayList<>();
            List<UserDto> participants = userJoinedChatRoomDto.getParticipants();
            for (UserDto participant : participants) {
                User user = new User(participant.getEmail(), participant.getPassword(), participant.getName(), null, participant.getGender());
                userList.add(user);
            }

            Optional<String> optionalLastMessage = Optional.ofNullable(userJoinedChatRoomDto.getLastMessage());
            String lastMessage = optionalLastMessage.orElse("");

            //LocalDateTime lastSentTime = userJoinedChatRoomDto.getLastSentTime();

            Optional<LocalDateTime> optionalLastSentTime = Optional.ofNullable(userJoinedChatRoomDto.getLastSentTime());
            String lastSentTimeString = optionalLastSentTime.map(LocalDateTime::toString).orElse("");

            ChatRoom chatRoom = new ChatRoom(chatRoomId,chatRoomName,userList,lastMessage,lastSentTimeString);
            Log.d("채팅방 목록",chatRoom.toString());
            chatRoomApiResponseList.add(chatRoom);
            // 특정 채널 구독
            webSocketViewModel.subscribeToChannel(chatRoomId);
        }
        return chatRoomApiResponseList;
    }

}
