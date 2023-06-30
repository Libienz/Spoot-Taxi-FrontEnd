package com.example.spoot_taxi_front;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChatFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private ChatRoomAdapter chatRoomAdapter;

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

        return view;
    }



}
